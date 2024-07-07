package fyi.atom.lifesuite.di.module.singleton

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object SingletonCoroutineScopeModule {
    @[Provides SingletonLifecycle]
    fun coroutineScope(): CoroutineScope = ProcessLifecycleOwner.get().lifecycleScope
}
