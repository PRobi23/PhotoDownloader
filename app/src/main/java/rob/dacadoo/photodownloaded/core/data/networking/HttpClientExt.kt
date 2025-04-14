package rob.dacadoo.photodownloaded.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodedPath
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import rob.dacadoo.photodownloaded.core.domain.util.DataError
import rob.dacadoo.photodownloaded.core.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

/**
 * Makes a GET request using Ktor's [HttpClient], appending the given [route] to the base URL.
 *
 * @param route The endpoint path (e.g., "/search/photos").
 * @param queryParameters Optional query parameters to include in the request.
 * @return A [Result] containing the parsed response of type [Response] or a [DataError.Network].
 */
suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> {
    return safeCall {
        get {
            url {
                encodedPath = route
                queryParameters.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
        }
    }
}

/**
 * Wraps a suspend network request with error handling and maps exceptions to [DataError.Network].
 *
 * @param execute A lambda to execute the HTTP request.
 * @return A [Result] with either the parsed response of type [T] or a mapped [DataError.Network] error.
 */
suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

/**
 * Converts an [HttpResponse] to a [Result], mapping the HTTP status code to a [DataError.Network] if needed.
 *
 * @param response The raw [HttpResponse] returned from the server.
 * @return A [Result.Success] if the status code is in the 2xx range, otherwise [Result.Error] with appropriate error.
 */
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> {
    return when (response.status.value) {
        in 200..299 -> Result.Success(response.body<T>())
        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Error(DataError.Network.CONFLICT)
        413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}

