package fyi.atom.lifesuite

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import fr.bipi.treessence.context.GlobalContext.startTimber
import fr.bipi.treessence.dsl.FileTreeScope
import fr.bipi.treessence.file.FileLoggerTree
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLogging()
    }

    private fun setupLogging() {
        startTimber {
            debugTree()
            fileTree {}
        }
    }
}