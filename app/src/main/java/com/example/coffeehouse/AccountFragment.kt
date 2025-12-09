package com.example.coffeehouse

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var ordersHistory: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersHistory = view.findViewById(R.id.ordersHistory)

        ordersHistory.setOnClickListener {
            val ordersHistoryFragment = OrdersHistoryFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ordersHistoryFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
