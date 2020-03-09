package com.apdallahy3.accenturetask.data.source.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Suppress("NAME_SHADOWING")
class ApiServiceFactory {

    companion object {

        private const val BASE_URL = ApiConstants.BASE_URL
        var ACCESS_TOKEN = ""

        fun getService(): ApiService {
            return synchronized(this) {

                val instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(provideOkHttpClient())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()
                    .create(ApiService::class.java)
                instance
            }
        }

        private fun provideOkHttpClient(): OkHttpClient {


            val client = OkHttpClient.Builder()
            client.addInterceptor(initializeHeaders(ACCESS_TOKEN))//Todo()

//            if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(interceptor)//Add Loggong Intercepter
//            }

            return client.build()
        }

        private fun initializeHeaders(token: String?): Interceptor {

            val headers = HashMap<String, String>()
            headers.put("Content-Type", "application/x-www-form-urlencoded")
            headers.put("charset", "utf-8")

            return Interceptor { chain ->
                val original = chain.request()
                val request: Request
                val builder = original.newBuilder()
                val headers = headers
                headers.forEach {
                    builder.header(it.key, it.value)
                }
                builder.method(original.method(), original.body())
                request = builder.build()
                chain.proceed(request)
            }

        }
    }
}