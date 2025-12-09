package com.example.coffeehouse

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import androidx.core.content.edit


class CartAdapter(
    private var items: MutableList<CartItem> = mutableListOf()
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.textName)
        val priceText: TextView = view.findViewById(R.id.textPrice)
        val imageView: ImageView = view.findViewById(R.id.imageDrink)
        val quantityText: TextView = view.findViewById(R.id.textQuantity)
        val removeButton: ImageButton = view.findViewById<ImageButton>(R.id.removeFromCart)
        val addButton: ImageButton = view.findViewById<ImageButton>(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_card, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position]
        val context = holder.itemView.context


        holder.nameText.text = cartItem.item.name
        holder.priceText.text = cartItem.item.price.toString()
        holder.quantityText.text = context.getString(R.string.item_quantity, cartItem.quantity)

        if (cartItem.item.image.isNotEmpty()) {
            Glide.with(holder.imageView.context).load(cartItem.item.image).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.jisung)
        }

        holder.removeButton.setOnClickListener {
            removeOne(holder.itemView.context, position)
        }
        holder.addButton.setOnClickListener {
            addOne(holder.itemView.context, position)
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
    private fun addOne(context: Context, position: Int){
        var cartItem = items[position]
        if (cartItem.quantity<=9){
            cartItem.quantity+=1
            notifyItemChanged(position)
        } else {
            MaterialAlertDialogBuilder(context, R.style.CoffeeAlertDialog)
                .setTitle(R.string.CartAlertDialogTitle)
                .setMessage(R.string.CartAlertDialogMessage)
                .setPositiveButton(R.string.Okay){_,_->

                }
                .show()
        }
        saveCart(context)
    }

    private fun saveCart(context: Context) {
        val sharedPref = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        sharedPref.edit {
            val gson = Gson()
            putString("cart_list", gson.toJson(items))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: MutableList<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun getItems(): List<CartItem> = items

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }


}
