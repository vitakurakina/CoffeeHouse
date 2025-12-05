package com.example.coffeehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCart)

        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadCartItems()

        return view
    }

    private fun loadCartItems() {
        val sharedPref = requireContext().getSharedPreferences("cart_prefs", android.content.Context.MODE_PRIVATE)
        val gson = Gson()
        val cartJson = sharedPref.getString("cart_list", "[]")
        val type = object : TypeToken<List<CartItem>>() {}.type
        val cartList: List<CartItem> = gson.fromJson(cartJson, type)

        if (cartList.isEmpty()) {
            //Toast.makeText(context, "Корзина пуста", Toast.LENGTH_SHORT).show()
        }

        cartAdapter.setItems(cartList as MutableList<CartItem>)
    }
}
