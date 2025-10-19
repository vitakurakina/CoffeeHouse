package com.example.coffeehouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeehouse.databinding.ActivityHelloWorldBinding

class HelloWorldActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelloWorldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hello_world)
    }

}