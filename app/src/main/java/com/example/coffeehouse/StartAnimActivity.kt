package com.example.coffeehouse

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StartAnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_anim)

        val logo = findViewById<ImageView>(R.id.splashLogo)

        val animation = AnimationUtils.loadAnimation(this, R.anim.start)
        logo.startAnimation(animation)

        logo.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}
