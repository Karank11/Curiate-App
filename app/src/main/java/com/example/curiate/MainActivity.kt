package com.example.curiate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.curiate.ui.collectionscreen.CollectionFragment
import com.example.curiate.ui.explorescreen.ExploreFragment
import com.example.curiate.ui.savedscreen.SavedScreenFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setupBottomBar()

        enableEdgeToEdge()
        setContentView(R.layout.activity_random)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
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