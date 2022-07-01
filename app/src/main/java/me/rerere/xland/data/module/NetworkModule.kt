package me.rerere.xland.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.xland.data.api.NimingbanAPI
import me.rerere.xland.data.api.RefAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideNimingbanAPI(okHttpClient: OkHttpClient): NimingbanAPI = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://nmbxd1.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NimingbanAPI::class.java)

    @Provides
    @Singleton
    fun provideRefAPI(okHttpClient: OkHttpClient): RefAPI = RefAPI(okHttpClient)
}