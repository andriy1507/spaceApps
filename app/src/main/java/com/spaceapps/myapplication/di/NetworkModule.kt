package com.spaceapps.myapplication.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.network.AuthInterceptor
import com.spaceapps.myapplication.network.AuthorizationApi
import com.spaceapps.myapplication.network.PostsApi
import com.spaceapps.myapplication.network.SpaceAppsAuthenticator
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor {
            Timber.tag("OkHttp").d(it)
        }.apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        authenticator: SpaceAppsAuthenticator
    ) = OkHttpClient.Builder().apply {
        addInterceptor(authInterceptor)
        authenticator(authenticator)
        addNetworkInterceptor(StethoInterceptor())
        addInterceptor(logger)
    }.build()


    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder().apply {
        baseUrl(BuildConfig.SERVER_URL)
        addConverterFactory(MoshiConverterFactory.create(moshi))
        client(client)
    }.build()

    @Provides
    @Singleton
    fun provideAuthorizationApi(retrofit: Retrofit): AuthorizationApi =
        retrofit.create(AuthorizationApi::class.java)

    @Provides
    @Singleton
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create(PostsApi::class.java)
}