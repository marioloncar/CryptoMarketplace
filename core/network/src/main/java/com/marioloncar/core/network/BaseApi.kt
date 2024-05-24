package com.marioloncar.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import timber.log.Timber

/**
 * A base API class that provides a method to execute HTTP requests using a provided [HttpClient].
 *
 * @constructor httpClient The [HttpClient] used for making HTTP requests.
 */
open class BaseApi(private val httpClient: HttpClient) {

    /**
     * Executes a suspending block of code using the provided [HttpClient] and handles any
     * [ResponseException] that may occur.
     */
    suspend fun <T> execute(block: suspend HttpClient.() -> T): T {
        return try {
            block(httpClient)
        } catch (responseException: ResponseException) {
            Timber.e("ResponseException", responseException.cause)
            throw responseException
        }
    }
}
