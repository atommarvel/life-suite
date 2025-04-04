package fyi.atom.linkshift

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchIntent()
        finish()
    }

    private fun launchIntent() {
        val uri = intent.data
        if (uri?.authority == "x.com") {
            val redirect = uri.buildUpon().authority("xcancel.com").build()
            val intent = Intent(Intent.ACTION_VIEW, redirect)
            startActivity(intent)
        }
    }
}
