package skalii.testjob.osmand.data.remote


import okhttp3.ResponseBody

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Streaming


interface RemoteApi {

    @Headers("Content-Type: application/zip")
    @GET("download.php")
    @Streaming
    suspend fun getSomeZipFiles(
        @Query(value = "file") file: String,
        @Query(value = "standard") standard: String = "yes"
    ): ResponseBody?

}