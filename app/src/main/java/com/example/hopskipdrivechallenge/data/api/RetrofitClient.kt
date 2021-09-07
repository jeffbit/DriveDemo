package com.example.hopskipdrivechallenge.data.api

import com.example.hopskipdrivechallenge.BuildConfig.DEBUG
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://storage.googleapis.com/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
        .apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    interceptors().add(interceptor)
                    return@Interceptor chain.proceed(builder.build())
                }
            )

            if (DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = (HttpLoggingInterceptor.Level.BODY)
                addInterceptor(logging)
            }
        }.build()




    private fun retrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)

            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val hopSkipDriveApi: HopSkipDriveApi = retrofit().create(HopSkipDriveApi::class.java)
}