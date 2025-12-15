package com.example.coffeehouse

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var loginText: TextView
    private lateinit var ordersHistory: MaterialButton
    private lateinit var signUp: MaterialButton
    private lateinit var signIn: MaterialButton

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginText = view.findViewById(R.id.loginViewed)
        ordersHistory = view.findViewById(R.id.ordersHistory)
        signUp = view.findViewById(R.id.toSignUpButton)
        signIn = view.findViewById(R.id.toSignInButton)

        ordersHistory.setOnClickListener {
            val ordersHistoryFragment = OrdersHistoryFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ordersHistoryFragment)
                .addToBackStack(null)
                .commit()
        }

        signUp.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }

        signIn.setOnClickListener {
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateLoginText()
    }

    private fun updateLoginText() {
        val prefs = requireContext().getSharedPreferences("auth", 0)
        val login = prefs.getString("login", null)
        loginText.text = login ?: "Login"
    }
}
