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
    private var items: MutableList<MenuDrinksItem> = mutableListOf()
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.textName)
        val priceText: TextView = view.findViewById(R.id.textPrice)
        val imageView: ImageView = view.findViewById(R.id.imageDrink)
        val deleteButton: ImageView = view.findViewById(R.id.removeFromCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_card, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.nameText.text = item.name
        holder.priceText.text = item.price.toString()

        if (item.image.isNotEmpty()) {
            Glide.with(holder.imageView.context).load(item.image).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.jisung)
        }

        holder.deleteButton.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)

            val sharedPref = holder.itemView.context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            editor.putString("cart_list", gson.toJson(items))
            editor.apply()

            Toast.makeText(holder.itemView.context, "Товаров в корзине: ${items.size}", Toast.LENGTH_SHORT).show()
        }
    }

    fun setItems(newItems: List<MenuDrinksItem>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
