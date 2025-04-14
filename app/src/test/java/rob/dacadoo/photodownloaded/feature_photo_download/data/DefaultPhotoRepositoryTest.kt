package rob.dacadoo.photodownloaded.feature_photo_download.data

import MockPhotoSearchResultResponse
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import rob.dacadoo.photodownloaded.core.domain.util.Result

class DefaultPhotoRepositoryTest {
    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    private fun createHttpClient(engine: MockEngine): HttpClient = HttpClient(engine) {
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
        ioDispatcher = testDispatcher
    )

    @Test
    fun `when the api call succeeds then return the expected models`() = runTest {
        val name = "ios"
        val engine = MockEngine { request ->
            if (request.url.encodedPath.endsWith("/search/photos")) {
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

        assertThat(data.total).isEqualTo(10000)
        assertThat(data.totalPages).isEqualTo(1000)
        assertThat(data.results).hasSize(10)

        val firstPhoto = data.results.first()
        assertThat(firstPhoto.id).isEqualTo("A6JxK37IlPo")
        assertThat(firstPhoto.altDescription).isEqualTo("person holding space gray iPhone 7")
        assertThat(firstPhoto.user.username).isEqualTo("bhaguz")
    }
}