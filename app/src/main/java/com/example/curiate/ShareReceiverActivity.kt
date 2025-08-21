package com.example.curiate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.curiate.ui.SaveContentFragment

class ShareReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val args = Bundle()
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {text ->
                args.putString(Intent.EXTRA_TEXT, text)
            }
            intent.getStringExtra(Intent.EXTRA_SUBJECT)?.let {subject ->
                args.putString(Intent.EXTRA_SUBJECT, subject)
            }
            val saveContentFragment = SaveContentFragment.newInstance(args)
            saveContentFragment.show(supportFragmentManager, SaveContentFragment.TAG)
        }
    }
}