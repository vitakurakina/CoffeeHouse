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
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCart)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.make_an_order_sound)

        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadCartItems()

        val makeOrderButton = view.findViewById<MaterialButton>(R.id.makeAnOrder)
        makeOrderButton.setOnClickListener {
            if (cartAdapter.getItems().isEmpty()) {
                Toast.makeText(context, R.string.cart_is_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = Room.databaseBuilder(
                requireContext(),
                AppDatabase::class.java,
                "app_database"
            ).build()
            val orderDao = db.orderDao()

            CoroutineScope(Dispatchers.IO).launch {
                val order = OrderEntity(cartItems = cartAdapter.getItems())
                orderDao.insertOrder(order)

                withContext(Dispatchers.Main) {
                    cartAdapter.clearItems()
                    requireContext().getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
                        .edit { clear() }
                    playButtonSound()
                    }
                }

        }

        return view
    }

    private fun loadCartItems() {
        val sharedPref = requireContext().getSharedPreferences(
            "cart_prefs",
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val cartJson = sharedPref.getString("cart_list", "[]")
        val type = object : TypeToken<List<CartItem>>() {}.type
        val cartList: List<CartItem> = gson.fromJson(cartJson, type)

        cartAdapter.setItems(cartList.toMutableList())
    }

    private fun playButtonSound(){
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        mediaPlayer.release()
    }
}
