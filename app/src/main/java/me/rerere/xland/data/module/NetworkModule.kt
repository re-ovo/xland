package me.rerere.xland.data.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.xland.data.api.NimingbanAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val APP_ID = "xland"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @Provides
    @Singleton
    fun provideNimingbanAPI(okHttpClient: OkHttpClient): NimingbanAPI = Retrofit.Builder()
        .baseUrl("https://nmbxd1.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NimingbanAPI::class.java)
}