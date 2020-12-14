package skalii.testjob.osmand.data.remote


import android.content.Context

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.time.Duration

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit

import skalii.testjob.osmand.BuildConfig
import skalii.testjob.osmand.toast


object RemoteService {

    private const val URL = "http://download.osmand.net/"


    fun getServiceApi(context: Context): RemoteApi {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getClient(context))
            .build()
            .create(RemoteApi::class.java)
    }

    private fun getClient(context: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }
        client.connectTimeout(Duration.ofSeconds(3))

        client.addInterceptor(Interceptor { chain ->
            val request: Request = chain.request()
            val response: okhttp3.Response = try {
                chain.proceed(request)
            } catch (e: IOException) {

                when (e) {
                    is SocketTimeoutException, is SocketException -> throw IOException("Відсутнє підключення до серверу")
                    else -> throw IOException("Помилка при з'єднані")
                }
            }

            GlobalScope.launch(Dispatchers.Main) {
                when (response.code) {
                    404 -> context.toast("Server: 404")
                    500 -> context.toast("Server: 500")
                }
            }

            response
        })
        return client.build()
    }

}