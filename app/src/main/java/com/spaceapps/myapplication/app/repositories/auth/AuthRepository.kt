package com.spaceapps.myapplication.app.repositories.auth

import com.spaceapps.myapplication.app.repositories.auth.results.*

interface AuthRepository {
    suspend fun signIn(email: String, password: String): SignInResult

    suspend fun signUp(email: String, password: String): SignUpResult

    suspend fun signInWithGoogle(accessToken: String): SocialSignInResult

    suspend fun signInWithFacebook(accessToken: String): SocialSignInResult

    suspend fun signInWithApple(accessToken: String): SocialSignInResult

    suspend fun sendResetToken(email: String): SendResetTokenResult

    suspend fun verifyResetToken(email: String, token: String): VerifyResetTokenResult

    suspend fun resetPassword(email: String, token: String, password: String): ResetPasswordResult

    suspend fun logOut(): LogOutResult
}
