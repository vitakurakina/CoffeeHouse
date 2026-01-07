package com.example.coffeehouse

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.coffeehouse.network.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.make_an_order_sound)

        loadCartItems()

        val makeOrderButton = view.findViewById<MaterialButton>(R.id.makeAnOrder)
        makeOrderButton.setOnClickListener {
            if (cartAdapter.getItems().isEmpty()) {
                Toast.makeText(context, R.string.cart_is_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            completeOrder()
        }

        return view
    }

    private fun getCurrentUserId(): Int {
        val prefs = requireContext()
            .getSharedPreferences("auth", Context.MODE_PRIVATE)

            return prefs.getInt("userId", -1)

    }

    private fun completeOrder() {
        val totalPrice = cartAdapter.getTotalPrice()
        val bonus = (totalPrice * 0.1).toInt()
        val userId = getCurrentUserId()

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val order = OrderEntity(cartItems = cartAdapter.getItems())
                    db.orderDao().insertOrder(order)
                }

                RetrofitInstance.api.addBonus(
                    BonusRequest(
                        userId = userId,
                        totalPrice = +bonus
                    )
                )

                cartAdapter.clearItems()
                requireContext()
                    .getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
                    .edit { clear() }

                playButtonSound()

                Toast.makeText(
                    requireContext(),
                    getString(R.string.orderSuccess, bonus),
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), R.string.orderErr,
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }

    private fun loadCartItems() {
        val sharedPref = requireContext()
            .getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)

        val cartJson = sharedPref.getString("cart_list", "[]")
        val type = object : TypeToken<List<CartItem>>() {}.type
        val cartList: List<CartItem> = Gson().fromJson(cartJson, type)

        cartAdapter.setItems(cartList.toMutableList())
    }

    private fun playButtonSound() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
