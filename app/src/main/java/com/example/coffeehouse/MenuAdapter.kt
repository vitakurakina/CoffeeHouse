package com.example.coffeehouse

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson

class MenuAdapter(
    private var items: List<MenuDrinksItem> = listOf()
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.textName)
        val priceText: TextView = view.findViewById(R.id.textPrice)
        val descriptionText: TextView = view.findViewById(R.id.textDescription)
        val infoText: TextView = view.findViewById(R.id.textInfo)
        val imageView: ImageView = view.findViewById(R.id.imageDrink)
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

    fun setItems(newItems: List<MenuDrinksItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun addToCart(context: Context, item: MenuDrinksItem){
        val sharedPref = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val currentCartJson = sharedPref.getString("cart_list", "[]")
        val type = object : com.google.gson.reflect.TypeToken<MutableList<MenuDrinksItem>>() {}.type
        val cartList: MutableList<MenuDrinksItem> = gson.fromJson(currentCartJson, type)
        cartList.add(item)
        editor.putString("cart_list", gson.toJson(cartList))
        editor.apply()

    }
}
