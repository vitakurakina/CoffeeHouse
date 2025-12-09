package com.example.coffeehouse

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersHistoryAdapter(
    private var orders: List<OrderEntity> = listOf()
) : RecyclerView.Adapter<OrdersHistoryAdapter.OrdersHistoryViewHolder>() {

    class OrdersHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderIdText: TextView = view.findViewById(R.id.textOrderId)
        val orderItemsText: TextView = view.findViewById(R.id.textOrderItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_history_item, parent, false)
        return OrdersHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrdersHistoryViewHolder, position: Int) {
        val context = holder.itemView.context
        val order = orders[position]
        holder.orderIdText.text = context.getString(R.string.order_number, order.id)
        holder.orderItemsText.text = order.cartItems.joinToString { "${it.item.name} x${it.quantity}" }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setOrders(newOrders: List<OrderEntity>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
