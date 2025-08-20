package com.example.curiate

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        setupSearchBar(menu)
        return true
    }

    private fun setupSearchBar(menu: Menu?) {
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        searchView.maxWidth = pixels
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.background = ContextCompat.getDrawable(this, R.drawable.search_bar_bg)
        searchEditText.setTextColor(Color.BLACK)
        searchEditText.setHintTextColor(Color.GRAY)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
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