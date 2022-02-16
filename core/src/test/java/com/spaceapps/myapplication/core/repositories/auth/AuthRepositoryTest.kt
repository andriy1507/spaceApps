package com.spaceapps.myapplication.core.repositories.auth

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.spaceapps.myapplication.MainCoroutineRule
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.models.remote.auth.AuthTokenResponse
import com.spaceapps.myapplication.core.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.core.repositories.auth.results.*
import com.spaceapps.myapplication.core.utils.DeviceInfoProvider
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import com.spaceapps.myapplication.core.utils.MoshiConverters
import com.squareup.moshi.Moshi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime

@SmallTest
@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class AuthRepositoryTest {

    private val authTokenResponse = AuthTokenResponse(
        accessToken = "accessToken",
        accessTokenExp = LocalDateTime.now(),
        refreshToken = "refreshToken",
        refreshTokenExp = LocalDateTime.now()
    )

    private val dispatcher = StandardTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(dispatcher)

    private lateinit var repository: AuthRepository

    private lateinit var calls: AuthorizationCalls

    private val mockWebServer = MockWebServer()

    @Mock
    lateinit var dataStoreManager: DataStoreManager

    @Mock
    lateinit var deviceInfoProvider: DeviceInfoProvider

    @Before
    fun setUp() = runBlocking {
        mockWebServer.start()
        mockDeviceInfoProvider()
        repository = AuthRepositoryImpl(
            calls = mockAuthorizationCalls(),
            dataStoreManager = dataStoreManager,
            dispatchersProvider = object : DispatchersProvider {
                override val Main = dispatcher
                override val IO = dispatcher
                override val Default = dispatcher
                override val Unconfined = dispatcher
            },
            deviceInfoProvider = deviceInfoProvider
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Sign-In Success`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        val result = repository.signIn("", "")
        assertThat(result).isEqualTo(SignInResult.Success)
    }

    @Test
    fun `Sign-In Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.signIn("", "")
        assertThat(result).isEqualTo(SignInResult.Failure)
    }

    @Test
    fun `On Sign-In success tokens are saved`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        repository.signIn("", "")
        Mockito.verify(dataStoreManager, times(1)).storeTokens(authTokenResponse)
    }

    @Test
    fun `Sign-Up Success`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        val result = repository.signUp("", "")
        assertThat(result).isEqualTo(SignUpResult.Success)
    }

    @Test
    fun `Sign-Up Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.signUp("", "")
        assertThat(result).isEqualTo(SignUpResult.Failure)
    }

    @Test
    fun `On Sign-Up success tokens are saved`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        repository.signUp("", "")
        Mockito.verify(dataStoreManager, times(1)).storeTokens(authTokenResponse)
    }

    @Test
    fun `Sign-In with Google Success`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        val result = repository.signInWithGoogle("")
        assertThat(result).isEqualTo(SocialSignInResult.Success)
    }

    @Test
    fun `Sign-In with Google Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.signInWithGoogle("")
        assertThat(result).isEqualTo(SocialSignInResult.Failure)
    }

    @Test
    fun `On Google Sign-In success tokens are saved`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        repository.signInWithGoogle("")
        Mockito.verify(dataStoreManager, times(1)).storeTokens(authTokenResponse)
    }

    @Test
    fun `Sign-In with Facebook Success`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        val result = repository.signInWithFacebook("")
        assertThat(result).isEqualTo(SocialSignInResult.Success)
    }

    @Test
    fun `Sign-In with Facebook Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.signInWithFacebook("")
        assertThat(result).isEqualTo(SocialSignInResult.Failure)
    }

    @Test
    fun `On Facebook Sign-In success tokens are saved`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        repository.signInWithFacebook("")
        Mockito.verify(dataStoreManager, times(1)).storeTokens(authTokenResponse)
    }

    @Test
    fun `Sign-In with Apple Success`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        val result = repository.signInWithApple("")
        assertThat(result).isEqualTo(SocialSignInResult.Success)
    }

    @Test
    fun `Sign-In with Apple Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.signInWithApple("")
        assertThat(result).isEqualTo(SocialSignInResult.Failure)
    }

    @Test
    fun `On Apple Sign-In success tokens are saved`() = runTest {
        mockWebServer.enqueue(provideAuthTokenResponse())
        repository.signInWithApple("")
        Mockito.verify(dataStoreManager, times(1)).storeTokens(authTokenResponse)
    }

    @Test
    fun `Send reset code Success`() = runTest {
        mockWebServer.enqueue(provideSuccessResponse())
        val result = repository.sendResetCode("")
        assertThat(result).isEqualTo(SendResetCodeResult.Success)
    }

    @Test
    fun `Send reset code Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.sendResetCode("")
        assertThat(result).isEqualTo(SendResetCodeResult.Failure)
    }

    @Test
    fun `Verify reset code Success`() = runTest {
        mockWebServer.enqueue(provideSuccessResponse())
        val result = repository.verifyResetCode("", "")
        assertThat(result).isEqualTo(VerifyResetCodeResult.Success)
    }

    @Test
    fun `Verify reset code Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.verifyResetCode("", "")
        assertThat(result).isEqualTo(VerifyResetCodeResult.Failure)
    }

    @Test
    fun `Reset password Success`() = runTest {
        mockWebServer.enqueue(provideSuccessResponse())
        val result = repository.resetPassword("", "", "")
        assertThat(result).isEqualTo(ResetPasswordResult.Success)
    }

    @Test
    fun `Reset password Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.resetPassword("", "", "")
        assertThat(result).isEqualTo(ResetPasswordResult.Failure)
    }

    @Test
    fun `Log out Success`() = runTest {
        mockWebServer.enqueue(provideSuccessResponse())
        val result = repository.logOut()
        assertThat(result).isEqualTo(LogOutResult.Success)
    }

    @Test
    fun `Log out Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.logOut()
        assertThat(result).isEqualTo(LogOutResult.Failure)
    }

    @Test
    fun `Add device Success`() = runTest {
        mockWebServer.enqueue(provideSuccessResponse())
        val result = repository.addDevice("")
        assertThat(result).isEqualTo(AddDeviceResult.Success)
    }

    @Test
    fun `Add device Failure`() = runTest {
        mockWebServer.enqueue(provideErrorResponse())
        val result = repository.addDevice("")
        assertThat(result).isEqualTo(AddDeviceResult.Failure)
    }

    private fun mockDeviceInfoProvider() = runBlocking {
        `when`(deviceInfoProvider.getFirebaseInstallationId()).thenReturn("")
        `when`(deviceInfoProvider.getFirebaseMessagingToken()).thenReturn("")
        `when`(deviceInfoProvider.provideManufacturer()).thenReturn("")
        `when`(deviceInfoProvider.provideModel()).thenReturn("")
        `when`(deviceInfoProvider.provideOsVersion()).thenReturn("")
    }

    private fun mockAuthorizationCalls(): AuthorizationCalls {
        val moshi = Moshi.Builder()
            .add(MoshiConverters())
            .build()
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AuthorizationCalls::class.java)
    }

    private fun provideAuthTokenResponse(): MockResponse {
        val body = Moshi.Builder()
            .add(MoshiConverters())
            .build().adapter(AuthTokenResponse::class.java)
            .toJson(authTokenResponse)
        return MockResponse()
            .setResponseCode(200)
            .setBody(body)
    }

    private fun provideErrorResponse(): MockResponse {
        return MockResponse()
            .setResponseCode(400)
    }

    private fun provideSuccessResponse():MockResponse {
        return  MockResponse()
            .setResponseCode(200)
    }
}
