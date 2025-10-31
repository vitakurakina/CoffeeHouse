package com.example.coffeehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val menuFragment = MenuFragment()
    private val accountFragment = AccountFragment()
    private val offersFragment = OffersFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav_container)

        // Установим Home по умолчанию
        bottomNavigation.selectedItemId = R.id.nav_home

        // Показываем HomeFragment по умолчанию
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()

        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment = when(item.itemId) {
                R.id.nav_home -> homeFragment
                R.id.nav_menu -> menuFragment
                R.id.nav_account -> accountFragment
                R.id.nav_offers -> offersFragment
                else -> homeFragment
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }
    }
}
