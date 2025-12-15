package com.example.coffeehouse

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.coffeehouse.network.RetrofitInstance

class SignInActivity : AppCompatActivity() {
    private lateinit var emailTextEdit: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        emailTextEdit = findViewById(R.id.signInEmailEditText)
        passwordEditText = findViewById(R.id.signInPasswordEditText)
        signInButton = findViewById(R.id.signInButton)

        signInButton.setOnClickListener {
            val email = emailTextEdit.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, R.string.fillAll, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val request = SignInRequest(email, password)

        RetrofitInstance.api.signIn(request)
            .enqueue(object : retrofit2.Callback<AuthResponse> {
                override fun onResponse(
                    call: retrofit2.Call<AuthResponse>,
                    response: retrofit2.Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        val auth = response.body()
                        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                        prefs.edit {
                            putString("login", auth?.login)
                            putInt("user_id", auth?.user_id ?: -1)
                            putString("qr_token", auth?.qr_token)
                        }

                        Toast.makeText(this@SignInActivity, R.string.signInSuss, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@SignInActivity, R.string.signInErr, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@SignInActivity, R.string.errDuesignIn, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
