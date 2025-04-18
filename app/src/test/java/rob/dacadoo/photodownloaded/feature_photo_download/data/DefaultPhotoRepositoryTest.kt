package rob.dacadoo.photodownloaded.feature_photo_download.data

import MockPhotoSearchResultResponse
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import rob.dacadoo.photodownloaded.core.domain.util.DataError
import rob.dacadoo.photodownloaded.core.domain.util.Result
import rob.dacadoo.photodownloaded.feature_photo_download.domain.model.Photo

class DefaultPhotoRepositoryTest {
    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    private val apiKey = "api-key"

    private fun createHttpClient(engine: MockEngine): HttpClient = HttpClient(engine) {
        defaultRequest {
            host = "api.unsplash.com"
            url {
                protocol = URLProtocol.HTTP
            }
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createRepository(engine: MockEngine) = DefaultPhotoRepository(
        httpClient = createHttpClient(engine),
        ioDispatcher = testDispatcher,
        apiKey = apiKey
    )

    @Test
    fun `when the api call succeeds then return the expected models`() = runTest {
        val name = "ios"
        val engine = MockEngine { request ->
            if (request.url.toString() == "http://api.unsplash.com/search/photos?query=ios&client_id=${apiKey}") {
                respond(MockPhotoSearchResultResponse(), HttpStatusCode.OK, responseHeaders)
            } else {
                error("Unhandled ${request.url.encodedPath}")
            }
        }

        val repository = createRepository(engine)
        val response = repository.getPhotosByName(name)

        assertThat(response).isInstanceOf(Result.Success::class.java)

        val success = response as Result.Success
        val data = success.data

        assertThat(data.first()).isEqualTo(
            Photo(
                "https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wzOTIyODV8MHwxfHNlYXJjaHwxfHxpcGhvbmV8ZW58MHx8fHwxNzQ0NDM5MzY2fDA&ixlib=rb-4.0.3&q=80&w=200"
            )
        )
    }

    @Test
    fun `when the api call fails with UnresolvedAddressException then return NO_INTERNET error`() =
        runTest {
            val name = "ios"
            val engine = MockEngine { request ->
                if (request.url.toString() == "http://api.unsplash.com/search/photos?query=ios&client_id=${apiKey}") {
                    throw UnresolvedAddressException()
                } else {
                    error("Unhandled ${request.url.encodedPath}")
                }
            }

            val repository = createRepository(engine)
            val response = repository.getPhotosByName(name)

            assertThat(response).isInstanceOf(Result.Error::class.java)
            assertThat((response as Result.Error).error).isEqualTo(DataError.Network.NO_INTERNET)
        }

    @Test
    fun `when the api call fails with 401 then return UNAUTHORIZED error`() = runTest {
        mockHttpErrorWithErrorCodeAndExpectedResult(
            expectedError = DataError.Network.UNAUTHORIZED,
            httpStatusCode = HttpStatusCode.Unauthorized
        )
    }

    @Test
    fun `when the api call fails with 408 then return REQUEST_TIMEOUT error`() = runTest {
        mockHttpErrorWithErrorCodeAndExpectedResult(
            expectedError = DataError.Network.REQUEST_TIMEOUT,
            httpStatusCode = HttpStatusCode.RequestTimeout
        )
    }

    @Test
    fun `when the api call fails with 409 then return CONFLICT error`() = runTest {
        mockHttpErrorWithErrorCodeAndExpectedResult(
            expectedError = DataError.Network.CONFLICT,
            httpStatusCode = HttpStatusCode.Conflict
        )
    }

    @Test
    fun `when the api call fails with 413 then return PAYLOAD_TOO_LARGE error`() = runTest {
        mockHttpErrorWithErrorCodeAndExpectedResult(
            expectedError = DataError.Network.PAYLOAD_TOO_LARGE,
            httpStatusCode = HttpStatusCode.PayloadTooLarge
        )
    }

    @Test
    fun `when the api call fails with 429 then return TOO_MANY_REQUESTS error`() = runTest {
        mockHttpErrorWithErrorCodeAndExpectedResult(
            expectedError = DataError.Network.TOO_MANY_REQUESTS,
            httpStatusCode = HttpStatusCode.TooManyRequests
        )
    }

    @Test
    fun `when the api call fails with 503 then return SERVER_ERROR error`() = runTest {
        mockHttpErrorWithErrorCodeAndExpectedResult(
            expectedError = DataError.Network.SERVER_ERROR,
            httpStatusCode = HttpStatusCode.ServiceUnavailable
        )
    }


    private suspend fun mockHttpErrorWithErrorCodeAndExpectedResult(
        httpStatusCode: HttpStatusCode,
        expectedError: DataError.Network
    ) {
        val name = "ios"
        val engine = MockEngine { request ->
            if (request.url.toString() == "http://api.unsplash.com/search/photos?query=ios&client_id=${apiKey}") {
                respondError(httpStatusCode)
            } else {
                error("Unhandled ${request.url.encodedPath}")
            }
        }

        val repository = createRepository(engine)
        val response = repository.getPhotosByName(name)

        assertThat(response).isInstanceOf(Result.Error::class.java)
        assertThat((response as Result.Error).error).isEqualTo(expectedError)
    }
}