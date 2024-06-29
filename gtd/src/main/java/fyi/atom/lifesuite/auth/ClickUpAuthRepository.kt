package fyi.atom.lifesuite.auth

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

// TODO: inject?
private val Context.clickUpAuthDataStore: DataStore<Preferences> by preferencesDataStore(name = "clickup-auth")

private val AUTH_STATE_KEY = stringPreferencesKey("state")

private val DataStore<Preferences>.authStateFlow: Flow<AuthState?>
    get() = data.map { preferences ->
        preferences[AUTH_STATE_KEY]?.let { json ->
            try {
                Json.decodeFromString<AuthState>(json)
            } catch (e: Exception) {
                null
            }
        }
    }.distinctUntilChanged()

@Singleton
class ClickUpAuthRepository @Inject constructor(
    app: Application
) {
    // TODO: inject an application scope coroutinescope
    private val scope = CoroutineScope(Dispatchers.Default)

    private val dataStore = app.clickUpAuthDataStore
    val authState: Flow<AuthState?> = dataStore.authStateFlow

    private val _loginRequests = MutableSharedFlow<Unit>()
    val loginRequests: Flow<Unit> = _loginRequests.asSharedFlow()

    fun setAuthState(authState: AuthState) = scope.launch {
        dataStore.edit { settings ->
            val json = Json.encodeToString(authState)
            settings[AUTH_STATE_KEY] = json
        }
    }

    fun launchLoginRequest() = scope.launch {
        _loginRequests.emit(Unit)
    }
}

