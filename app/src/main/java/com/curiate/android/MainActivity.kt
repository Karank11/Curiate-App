package com.curiate.android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.curiate.android.ui.collectionscreen.CollectionFragment
import com.curiate.android.ui.explorescreen.ExploreFragment
import com.curiate.android.ui.savedscreen.SavedScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, SavedScreenFragment())
            }
        }

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupBottomBar()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar)) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(0, statusBarHeight, 0, 0)
            insets
        }
    }

    private fun setupBottomBar() {
        val bottomBar: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_saved -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SavedScreenFragment())
                        .commit()
                    true
                }
                R.id.item_explore -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ExploreFragment())
                        .commit()
                    true
                }
                R.id.item_collection -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CollectionFragment())
                        .commit()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}