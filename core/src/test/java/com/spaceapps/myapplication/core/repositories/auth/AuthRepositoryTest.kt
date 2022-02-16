package com.spaceapps.myapplication.core.repositories.auth

import androidx.test.filters.SmallTest
import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.spaceapps.myapplication.core.BuildConfig
import com.spaceapps.myapplication.core.local.TestDataStoreManager
import com.spaceapps.myapplication.core.models.remote.auth.AuthTokenResponse
import com.spaceapps.myapplication.core.network.calls.AuthorizationCalls
import com.spaceapps.myapplication.core.repositories.auth.results.*
import com.spaceapps.myapplication.core.utils.TestDeviceInfoProvider
import com.spaceapps.myapplication.core.utils.TestDispatchersProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import java.time.LocalDateTime

@SmallTest
@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class AuthRepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Sign-In Success`() = runTest {
        val response = provideSuccessAuthTokenResponse()
        val repository = provideRepository(response)
        val result = repository.signIn("", "")
        assertThat(result).isInstanceOf(SignInResult.Success::class)
    }

    @Test
    fun `Sign-In Failure`() = runTest {
        val response: Response<AuthTokenResponse> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.signIn("", "")
        assertThat(result).isInstanceOf(SignInResult.Failure::class)
    }

    @Test
    fun `Sign-Up Success`() = runTest {
        val response = provideSuccessAuthTokenResponse()
        val repository = provideRepository(response)
        val result = repository.signUp("", "")
        assertThat(result).isInstanceOf(SignUpResult.Success::class)
    }

    @Test
    fun `Sign-Up Failure`() = runTest {
        val response: Response<AuthTokenResponse> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.signUp("", "")
        assertThat(result).isInstanceOf(SignUpResult.Failure::class)
    }

    @Test
    fun `Sign-In with Google Success`() = runTest {
        val response = provideSuccessAuthTokenResponse()
        val repository = provideRepository(response)
        val result = repository.signInWithGoogle("")
        assertThat(result).isInstanceOf(SocialSignInResult.Success::class)
    }

    @Test
    fun `Sign-In with Google Failure`() = runTest {
        val response: Response<AuthTokenResponse> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.signInWithGoogle("")
        assertThat(result).isInstanceOf(SocialSignInResult.Failure::class)
    }

    @Test
    fun `Sign-In with Facebook Success`() = runTest {
        val response = provideSuccessAuthTokenResponse()
        val repository = provideRepository(response)
        val result = repository.signInWithFacebook("")
        assertThat(result).isInstanceOf(SocialSignInResult.Success::class)
    }

    @Test
    fun `Sign-In with Facebook Failure`() = runTest {
        val response: Response<AuthTokenResponse> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.signInWithGoogle("")
        assertThat(result).isInstanceOf(SocialSignInResult.Failure::class)
    }

    @Test
    fun `Sign-In with Apple Success`() = runTest {
        val response = provideSuccessAuthTokenResponse()
        val repository = provideRepository(response)
        val result = repository.signInWithApple("")
        assertThat(result).isInstanceOf(SocialSignInResult.Success::class)
    }

    @Test
    fun `Sign-In with Apple Failure`() = runTest {
        val response: Response<AuthTokenResponse> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.signInWithGoogle("")
        assertThat(result).isInstanceOf(SocialSignInResult.Failure::class)
    }

    @Test
    fun `Send reset code Success`() = runTest {
        val response: Response<Unit> = provideEmptySuccessResponse(200)
        val repository = provideRepository(response)
        val result = repository.sendResetCode("")
        assertThat(result).isInstanceOf(SendResetCodeResult.Success::class)
    }

    @Test
    fun `Send reset code Failure`() = runTest {
        val response: Response<Unit> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.sendResetCode("")
        assertThat(result).isInstanceOf(SendResetCodeResult.Failure::class)
    }

    @Test
    fun `Verify reset code Success`() = runTest {
        val response: Response<Unit> = provideEmptySuccessResponse(200)
        val repository = provideRepository(response)
        val result = repository.verifyResetCode("", "")
        assertThat(result).isInstanceOf(VerifyResetCodeResult.Success::class)
    }

    @Test
    fun `Verify reset code Failure`() = runTest {
        val response: Response<Unit> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.verifyResetCode("", "")
        assertThat(result).isInstanceOf(VerifyResetCodeResult.Failure::class)
    }

    @Test
    fun `Reset password Success`() = runTest {
        val response: Response<Unit> = provideEmptySuccessResponse(200)
        val repository = provideRepository(response)
        val result = repository.resetPassword("", "", "")
        assertThat(result).isInstanceOf(ResetPasswordResult.Success::class)
    }

    @Test
    fun `Reset password Failure`() = runTest {
        val response: Response<Unit> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.resetPassword("", "", "")
        assertThat(result).isInstanceOf(ResetPasswordResult.Failure::class)
    }

    @Test
    fun `Log out Success`() = runTest {
        val response: Response<Unit> = provideEmptySuccessResponse(200)
        val repository = provideRepository(response)
        val result = repository.logOut()
        assertThat(result).isInstanceOf(LogOutResult.Success::class)
    }

    @Test
    fun `Log out Failure`() = runTest {
        val response: Response<Unit> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.logOut()
        assertThat(result).isInstanceOf(LogOutResult.Failure::class)
    }

    @Test
    fun `Add device Success`() = runTest {
        val response: Response<Unit> = provideEmptySuccessResponse(200)
        val repository = provideRepository(response)
        val result = repository.addDevice("")
        assertThat(result).isInstanceOf(AddDeviceResult.Success::class)
    }

    @Test
    fun `Add device Failure`() = runTest {
        val response: Response<Unit> = provideEmptyErrorResponse(400)
        val repository = provideRepository(response)
        val result = repository.addDevice("")
        assertThat(result).isInstanceOf(AddDeviceResult.Failure::class)
    }

    private fun provideRepository(response: Any?): AuthRepository {
        return AuthRepositoryImpl(
            calls = provideAuthCalls(response),
            dataStoreManager = TestDataStoreManager(),
            dispatchersProvider = TestDispatchersProvider(),
            deviceInfoProvider = TestDeviceInfoProvider()
        )
    }

    private fun provideAuthCalls(response: Any?): AuthorizationCalls {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .build()
        val mock = MockRetrofit.Builder(retrofit)
            .build()
        return mock.create(AuthorizationCalls::class.java).returningResponse(response)
    }

    private fun provideSuccessAuthTokenResponse() = AuthTokenResponse(
        accessToken = "",
        accessTokenExp = LocalDateTime.now(),
        refreshToken = "",
        refreshTokenExp = LocalDateTime.now()
    )

    private fun <T> provideEmptyErrorResponse(code: Int) = Response.error<T>(code, "".toResponseBody())

    private fun <T> provideEmptySuccessResponse(code: Int) = Response.success<T>(code, null)
}
