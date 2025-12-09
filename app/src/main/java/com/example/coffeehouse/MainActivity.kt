package com.example.coffeehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {

    private val menuFragment = MenuFragment()
    private val accountFragment = AccountFragment()
    private val offersFragment = OffersFragment()
    private val mapActivity = MapFragment()
    private val cartFragment = CartFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("3e80b4c0-83c7-4056-aa07-5cd2bea38401")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_home)


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_nav_container)

        bottomNavigation.selectedItemId = R.id.nav_menu

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, menuFragment)
            .commit()

        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment = when(item.itemId) {
                R.id.nav_map -> mapActivity
                R.id.nav_menu -> menuFragment
                R.id.nav_account -> accountFragment
                R.id.nav_offers -> offersFragment
                R.id.nav_cart -> cartFragment
                else -> menuFragment
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }
    }
}
