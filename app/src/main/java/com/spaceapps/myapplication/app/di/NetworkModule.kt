package com.spaceapps.myapplication.app.di

import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.app.network.AuthInterceptor
import com.spaceapps.myapplication.app.network.SpaceAppsAuthenticator
import com.spaceapps.myapplication.app.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.app.network.calls.LocationsCalls
import com.spaceapps.myapplication.app.network.calls.NotificationsCalls
import com.spaceapps.myapplication.app.network.calls.ProfileCalls
import com.spaceapps.myapplication.utils.QueryEnumConverterFactory
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

    private const val OKHTTP_LOGGING_TAG = "OkHttp"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor { Timber.tag(OKHTTP_LOGGING_TAG).d(it) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logger: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        authenticator: SpaceAppsAuthenticator
    ) = OkHttpClient.Builder().apply {
        addInterceptor(authInterceptor)
        authenticator(authenticator)
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
        addConverterFactory(QueryEnumConverterFactory)
        callFactory { client.newCall(it) }
    }.build()

    @Provides
    @Singleton
    fun provideAuthorizationCalls(retrofit: Retrofit): AuthorizationCalls =
        retrofit.create(AuthorizationCalls::class.java)

    @Provides
    @Singleton
    fun provideLocationsCalls(retrofit: Retrofit): LocationsCalls =
        retrofit.create(LocationsCalls::class.java)

    @Provides
    @Singleton
    fun provideProfileCalls(retrofit: Retrofit): ProfileCalls =
        retrofit.create(ProfileCalls::class.java)

    @Provides
    @Singleton
    fun provideNotificationsCalls(retrofit: Retrofit): NotificationsCalls =
        retrofit.create(NotificationsCalls::class.java)
}
