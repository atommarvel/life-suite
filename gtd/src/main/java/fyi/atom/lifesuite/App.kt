package fyi.atom.lifesuite

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.bipi.treessence.context.GlobalContext.startTimber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLogging()
    }

    private fun setupLogging() {
        startTimber {
            debugTree()
        }
    }
}
