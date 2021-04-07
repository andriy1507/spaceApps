package com.spaceapps.myapplication.features.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class AuthFragment : ComposableFragment() {

    private val vm by viewModels<AuthViewModel>()
    private val fbCallbackManager by lazy { CallbackManager.Factory.create() }
    private val googleSignIn = registerForActivityResult(StartActivityForResult()) { result ->
        GoogleSignIn.getSignedInAccountFromIntent(result.data).addOnSuccessListener { account ->
            val token = account.idToken.orEmpty()
            Timber.d("Login successful. Token: $token")
            vm.signInWithGoogle(accessToken = token)
        }.addOnFailureListener { e -> Timber.e(e) }
    }

    @Composable
    override fun Content() = AuthScreen(vm)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.events.onEach {
            when (it) {
                is SignInWithGoogle -> signInWithGoogle()
                is SignInWithFacebook -> signInWithFacebook()
                is SignInWithApple -> signInWithApple()
                is ShowError -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                else -> Unit
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun signInWithGoogle() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(requireContext(), options)
        googleSignIn.launch(client.signInIntent)
    }

    private fun signInWithFacebook() {
        val manager = LoginManager.getInstance()
        manager.registerCallback(
            fbCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val token = result.accessToken.token
                    Timber.d("Login successful. Token: $token")
                    vm.signInWithFacebook(accessToken = token)
                }

                override fun onCancel() {
                    Timber.d("Login canceled")
                }

                override fun onError(error: FacebookException) {
                    Timber.e(error)
                }
            }
        )
        manager.logIn(this, listOf("email"))
    }

    private fun signInWithApple() {
        val provider = OAuthProvider.newBuilder("apple.com")
            .apply { scopes = listOf("email, name") }.build()
        FirebaseAuth.getInstance().startActivityForSignInWithProvider(requireActivity(), provider)
            .addOnSuccessListener {
                val token = (it.credential as OAuthCredential).idToken
                Timber.d("Login successful. Token: $token")
                vm.signInWithApple(accessToken = token)
            }
            .addOnFailureListener { Timber.e(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
