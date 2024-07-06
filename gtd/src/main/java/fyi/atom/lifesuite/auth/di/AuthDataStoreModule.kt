package fyi.atom.lifesuite.auth.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDataStoreModule {
    @[Provides Singleton ClickUpAuthDataStore]
    fun authDataStore(app: Application): AuthDataStore = app.clickUpAuthDataStore
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ClickUpAuthDataStore

private val Context.clickUpAuthDataStore: DataStore<Preferences> by preferencesDataStore(name = "clickup-auth")

val AUTH_STATE_KEY: Preferences.Key<String> = stringPreferencesKey("state")

typealias AuthDataStore = DataStore<Preferences>