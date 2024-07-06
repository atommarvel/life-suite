package fyi.atom.lifesuite.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import fyi.atom.lifesuite.auth.di.AUTH_STATE_KEY
import fyi.atom.lifesuite.auth.di.AuthDataStore
import fyi.atom.lifesuite.auth.di.ClickUpAuthDataStore
import fyi.atom.lifesuite.di.module.singleton.SingletonLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClickUpAuthRepository @Inject constructor(
    @SingletonLifecycle
    private val scope: CoroutineScope,
    @ClickUpAuthDataStore
    private val dataStore: AuthDataStore
) {
    val authState: Flow<AuthState?> = dataStore.authStateFlow

    private val _loginRequests = MutableSharedFlow<Unit>()
    val loginRequests: Flow<Unit> = _loginRequests.asSharedFlow()

    fun setAuthState(authState: AuthState) = scope.launch {
        dataStore.edit { settings ->
            val json = Json.encodeToString(authState)
            settings.authState = json
        }
    }

    fun launchLoginRequest() = scope.launch {
        _loginRequests.emit(Unit)
    }
}

private val DataStore<Preferences>.authStateFlow: Flow<AuthState?>
    get() = data.map { preferences ->
        preferences.authState?.let { json ->
            try {
                Json.decodeFromString<AuthState>(json)
            } catch (e: Exception) {
                null
            }
        }
    }.distinctUntilChanged()

private val Preferences.authState: String?
    get() = get(AUTH_STATE_KEY)

private var MutablePreferences.authState: String?
    get() = get(AUTH_STATE_KEY)
    set(value) {
        if (value != null) {
            set(AUTH_STATE_KEY, value)
        } else {
            remove(AUTH_STATE_KEY)
        }
    }


