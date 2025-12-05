package com.example.coffeehouse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

class CartAdapter(
    private var items: MutableList<CartItem> = mutableListOf()
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.textName)
        val priceText: TextView = view.findViewById(R.id.textPrice)
        val imageView: ImageView = view.findViewById(R.id.imageDrink)
        val quantityText: TextView = view.findViewById(R.id.textQuantity)
        val deleteButton: ImageButton = view.findViewById(R.id.removeFromCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_card, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]


        holder.nameText.text = cartItem.item.name
        holder.priceText.text = cartItem.item.price.toString()
        holder.quantityText.text = "x ${cartItem.quantity}"

        if (cartItem.item.image.isNotEmpty()) {
            Glide.with(holder.imageView.context).load(cartItem.item.image).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.jisung)
        }

        holder.deleteButton.setOnClickListener {
            removeOne(holder.itemView.context, position)
//            items.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, items.size)
//
//            val sharedPref = holder.itemView.context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
//            val editor = sharedPref.edit()
//            val gson = Gson()
//            editor.putString("cart_list", gson.toJson(items))
//            editor.apply()
//
//            Toast.makeText(holder.itemView.context, "Товаров в корзине: ${items.size}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeOne(context: Context, position: Int) {
        val cartItem = items[position]

        if (cartItem.quantity > 1) {
            cartItem.quantity -= 1
            notifyItemChanged(position)
        } else {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }

        saveCart(context)
    }

    private fun saveCart(context: Context) {
        val sharedPref = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        editor.putString("cart_list", gson.toJson(items))
        editor.apply()
    }

    fun setItems(newItems: List<CartItem>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
