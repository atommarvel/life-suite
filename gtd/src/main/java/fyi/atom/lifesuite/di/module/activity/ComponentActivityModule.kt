package fyi.atom.lifesuite.di.module.activity

import android.app.Activity
import androidx.activity.ComponentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ComponentActivityModule {
    @Provides
    fun componentActivity(activity: Activity): ComponentActivity = activity as ComponentActivity
}