package com.example.coffeehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersHistoryFragment : Fragment() {

    private lateinit var ordersHistoryAdapter: OrdersHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders_history, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewOrderHistory)

        ordersHistoryAdapter = OrdersHistoryAdapter()
        recyclerView.adapter = ordersHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadOrders()

        return view
    }

    private fun loadOrders() {
        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
        val orderDao = db.orderDao()

        CoroutineScope(Dispatchers.IO).launch {
            val orders = orderDao.getAllOrders()
            withContext(Dispatchers.Main) {
                ordersHistoryAdapter.setOrders(orders)
            }
        }
    }
}
