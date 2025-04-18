package rob.dacadoo.photodownloaded.core.domain.util

typealias RootError = Error
typealias EmptyResult<E> = Result<Unit, E>

/**
 * Represents a computation that can either result in a success with a value of type [D],
 * or fail with an error of type [E], which extends [RootError].
 *
 * Result is a sealed interface that encompasses two possible outcomes:
 * - [Success]: Indicates successful computation with resulting data.
 * - [Error]: Indicates a failed computation with an error description.
 *
 * @param D The type of the data in the successful result.
 * @param E The type of the error in the erroneous result, extending [RootError].
 */
sealed interface Result<out D, out E : Error> {
    data class Success<out D>(
        val data: D
    ) : Result<D, Nothing>

    data class Error<out E : RootError>(
        val error: E
    ) : Result<Nothing, E>
}

/**
 * Transforms the successful result of a [Result] using the given [map] function,
 * while leaving the error unchanged.
 *
 * This function is useful for converting the success data from one type to another
 * (e.g., mapping a data model to a domain model), without modifying the error path.
 *
 * @param map A function that takes the success value [T] and returns a new value of type [R].
 * @return A [Result.Success] with the transformed value of type [R],
 *         or a [Result.Error] if the original result was an error.
 *
 * @receiver A [Result] that contains either a value of type [T] or an error of type [E].
 */
inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

/**
 * Applies one of two functions to the contained value based on whether it is a [Result.Success] or [Result.Error].
 *
 * This extension function allows processing a [Result] by providing:
 * - A [onSuccess] function that is invoked if the result is a success.
 * - A [onFailure] function that is invoked if the result is an error.
 *
 * @param onSuccess A function to execute with the successful data if the result is a [Result.Success].
 * @param onFailure A function to execute with the error data if the result is a [Result.Error].
 * @param D The type of the data contained in the [Result.Success].
 * @param E The type of the error extending [RootError] contained in the [Result.Error].
 * @param R The return type of the functions [onSuccess] and [onFailure], which should be the same type.
 * @return The result of either the [onSuccess] function or the [onFailure] function, depending on the type of result.
 */
inline fun <D, E : RootError, R> Result<D, E>.fold(
    onSuccess: (D) -> R,
    onFailure: (E) -> R,
): R =
    when (this) {
        is Result.Success -> onSuccess(this.data)
        is Result.Error -> onFailure(this.error)
    }
