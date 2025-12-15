package com.example.coffeehouse

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeehouse.network.RetrofitInstance

class AuthActivity : AppCompatActivity(){

    private lateinit var loginTextEdit: EditText
    private lateinit var emailTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var confirmPasswordTextEdit: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        loginTextEdit = findViewById<EditText>(R.id.loginEditText)
        emailTextEdit = findViewById<EditText>(R.id.emailEditText)
        passwordTextEdit = findViewById<EditText>(R.id.passwordEditText)
        confirmPasswordTextEdit = findViewById<EditText>(R.id.confirmPasswordEditText)
        signUpButton = findViewById<Button>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val login = loginTextEdit.text.toString()
            val email = emailTextEdit.text.toString()
            val password = passwordTextEdit.text.toString()
            val confirmPassword = confirmPasswordTextEdit.text.toString()

            if (password != confirmPassword){
                Toast.makeText(this, R.string.passwordsDontMatch, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (login.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, R.string.fillAll, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (login.length>15){
                Toast.makeText(this, R.string.loginShorter, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length<6 || password.length >20){
                Toast.makeText(this, R.string.passwordLonger, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createUser(login, email, password)
        }
    }

    private fun createUser(login: String, email: String, password: String) {

        val request = SignUpRequest(login, email, password
        )

        RetrofitInstance.api.signUp(request)
            .enqueue(object : retrofit2.Callback<AuthResponse> {

                override fun onResponse(
                    call: retrofit2.Call<AuthResponse>,
                    response: retrofit2.Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AuthActivity, R.string.regSuss, Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this@AuthActivity, R.string.regErr, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@AuthActivity, R.string.errDueReg, Toast.LENGTH_SHORT).show()
                }
            })
    }
}