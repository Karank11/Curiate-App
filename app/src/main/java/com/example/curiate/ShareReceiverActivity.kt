package com.example.curiate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.curiate.ui.SaveContentFragment

class ShareReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val saveContentFragment = SaveContentFragment.newInstance("url")
        saveContentFragment.show(supportFragmentManager, SaveContentFragment.TAG)
    }
}