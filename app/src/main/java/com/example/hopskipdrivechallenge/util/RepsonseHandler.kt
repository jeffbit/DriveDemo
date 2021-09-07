package com.example.hopskipdrivechallenge.util

import retrofit2.Response
import timber.log.Timber
import java.lang.Exception

//todo: future update: add fun to check network connection.

class ResponseHandler() {

    fun <T> process(response: Response<T>): Result<T> {
        return try {
            val body = response.body()
            when (body != null) {
                true -> {
                    Timber.d("SUCCESSFUL RESPONSE: $body")
                    Result.success(body)
                }
                false -> {
                    Timber.d("FAILED RESPONSE: ${response.errorBody().toString()}")
                    Result.failure(Exception())
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}