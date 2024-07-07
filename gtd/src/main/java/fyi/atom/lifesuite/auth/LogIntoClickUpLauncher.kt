package fyi.atom.lifesuite.auth

import android.app.Activity.RESULT_OK
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.scopes.ActivityScoped
import fyi.atom.lifesuite.BuildConfig
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientSecretBasic
import timber.log.Timber
import javax.inject.Inject

/**
 * https://clickup.com/api/developer-portal/authentication#step-1-create-an-oauth-app
 */
@ActivityScoped
class LogIntoClickUpLauncher
    @Inject
    constructor(
        private val activity: ComponentActivity,
        private val clickUpAuthRepository: ClickUpAuthRepository
    ) {
        private val authEndpoint = "https://app.clickup.com/api"
        private val tokenEndpoint = "https://api.clickup.com/api/v2/oauth/token"
        private val config = AuthorizationServiceConfiguration(authEndpoint.toUri(), tokenEndpoint.toUri())
        private var service: AuthorizationService? = null

        private val launcher =
            activity.registerForActivityResult(StartActivityForResult()) { intent ->
                if (intent.resultCode == RESULT_OK) {
                    val data = requireNotNull(intent.data)
                    val ex = AuthorizationException.fromIntent(data)
                    val result = AuthorizationResponse.fromIntent(data)

                    if (ex != null) {
                        Timber.e(ex)
                    } else {
                        val secret = ClientSecretBasic(BuildConfig.CLICKUP_CLIENT_SECRET)
                        val tokenRequest = requireNotNull(result?.createTokenExchangeRequest())
                        service?.performTokenRequest(tokenRequest, secret) { res, exception ->
                            if (exception != null) {
                                Timber.e(exception)
                            } else {
                                Timber.d(res?.jsonSerializeString())
                                val authState =
                                    AuthState(
                                        accessToken = requireNotNull(res?.accessToken)
                                    )
                                clickUpAuthRepository.setAuthState(authState)
                            }
                        }
                    }
                }
            }

        init {
            initServiceInLifecycle()
            listenForLoginRequests()
        }

        private fun initServiceInLifecycle() {
            activity.lifecycle.coroutineScope.launch {
                activity.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    try {
                        service = AuthorizationService(activity)
                        awaitCancellation()
                    } finally {
                        service?.dispose()
                        service = null
                    }
                }
            }
        }

        private fun listenForLoginRequests() {
            activity.lifecycle.coroutineScope.launch {
                activity.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    clickUpAuthRepository.loginRequests.collect {
                        requestLogin()
                    }
                }
            }
        }

        private fun requestLogin() {
            val req: AuthorizationRequest =
                AuthorizationRequest
                    .Builder(
                        config,
                        BuildConfig.CLICKUP_CLIENT_ID,
                        AuthorizationRequest.ResponseMode.QUERY,
                        "https://atom.fyi/lifesuite/oauth-redirect".toUri()
                    ).build()
            service?.getAuthorizationRequestIntent(req)?.let {
                launcher.launch(it)
            }
        }
    }