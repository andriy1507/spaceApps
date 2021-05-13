package com.spaceapps.myapplication.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.spaceapps.myapplication.BuildConfig
import com.spaceapps.myapplication.network.*
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
        callFactory { client.newCall(it) }
    }.build()

    @Provides
    @Singleton
    fun provideAuthorizationApi(retrofit: Retrofit): AuthorizationApi =
        retrofit.create(AuthorizationApi::class.java)

    @Provides
    @Singleton
    fun provideFeedsApi(retrofit: Retrofit): FeedsApi = retrofit.create(FeedsApi::class.java)

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi = retrofit.create(ChatApi::class.java)

    @Provides
    @Singleton
    fun provideUploadsApi(retrofit: Retrofit): UploadsApi = retrofit.create(UploadsApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi =
        retrofit.create(NotificationsApi::class.java)

    @Provides
    @Singleton
    fun provideStaticContentApi(retrofit: Retrofit): LegalApi =
        retrofit.create(LegalApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideSettingsApi(retrofit: Retrofit): SettingsApi =
        retrofit.create(SettingsApi::class.java)

    @Provides
    @Singleton
    fun provideToolsApi(retrofit: Retrofit): ToolsApi = retrofit.create(ToolsApi::class.java)
}
