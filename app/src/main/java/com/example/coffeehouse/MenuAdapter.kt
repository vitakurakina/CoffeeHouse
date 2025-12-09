package com.example.coffeehouse

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import androidx.core.content.edit

class MenuAdapter(
    private var items: List<MenuItem> = listOf()
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.textName)
        val priceText: TextView = view.findViewById(R.id.textPrice)
        val descriptionText: TextView = view.findViewById(R.id.textDescription)
        val infoText: TextView = view.findViewById(R.id.textInfo)
        val imageView: ImageView = view.findViewById(R.id.imageDrink)
        val infoCategory: TextView = view.findViewById<TextView>(R.id.textCategory)
        val addToCartButton: ImageView = view.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item_card, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.nameText.text = item.name
        holder.priceText.text = item.price
        holder.descriptionText.text = item.description
        holder.infoCategory.text=item.category
        holder.infoText.text = item.info


        if (item.image.isNotEmpty()) {
            Glide.with(holder.imageView.context)
                .load(item.image)
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.jisung) //Это просто смешная картинка с моим любимым кпоп айдолом Хан Джисоном.
                                                                // Я подумала что будет очень иронично вставить его фото сюда,
                                                                // потому что его имя "jisung" и слово "json" произносятся одинакого
        }
        holder.addToCartButton.setOnClickListener {
            addToCart(holder.itemView.context, item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<MenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun addToCart(context: Context, newItem: MenuItem){
        val sharedPref = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        sharedPref.edit {
            val gson = Gson()
            val currentCartJson = sharedPref.getString("cart_list", "[]")
            val type = object : com.google.gson.reflect.TypeToken<MutableList<CartItem>>() {}.type
            val cartList: MutableList<CartItem> = gson.fromJson(currentCartJson, type)

            val existingItem = cartList.firstOrNull { it.item.name == newItem.name }


            if (existingItem != null) {
                existingItem.quantity += 1
            } else {
                cartList.add(CartItem(newItem, 1))
            }
            putString("cart_list", gson.toJson(cartList))
        }

    }
}
