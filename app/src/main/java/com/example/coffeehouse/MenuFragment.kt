package com.example.coffeehouse

import android.Manifest
import android.R.string
import android.util.Log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeehouse.network.RetrofitInstance.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.io.File

class MenuFragment : Fragment() {

    private lateinit var adapter: MenuAdapter


    fun Context.isNetworkAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMenu)

        adapter = MenuAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadMenu()

        return view
    }

    private fun loadMenu() {
        api.getMenu().enqueue(object : Callback<List<MenuDrinksItem>> {
            override fun onResponse(call: Call<List<MenuDrinksItem>>, response: Response<List<MenuDrinksItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.setItems(it)
                        saveJSON(it)

                    }
                }
            }
            override fun onFailure(call: Call<List<MenuDrinksItem>>, t: Throwable) {
                if (context?.isNetworkAvailable() == true){
                    Toast.makeText(context, "Ошибка загрузки меню с сервера", Toast.LENGTH_SHORT).show()
                    loadMenuFromJSON()
                }else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.alert_message)
                        .setNeutralButton(R.string.Okay){_,_->

                        }
                        .show()
                }
            }
        })
    }

    private fun saveJSON(menuList: List<MenuDrinksItem>){
        val gson = Gson()
        val jsonString = gson.toJson(menuList)
        val file = File(requireContext().filesDir, "menu.json")
        file.writeText(jsonString)
        Toast.makeText(context, "JSON сохранён", Toast.LENGTH_SHORT).show()

    }

    private fun loadMenuFromJSON() {
        val file = File(requireContext().filesDir, "menu.json")
        if (file.exists()) {
            val jsonString = file.readText()
            val gson = Gson()
            val menuList = gson.fromJson(jsonString, Array<MenuDrinksItem>::class.java).toList()
            adapter.setItems(menuList)
            Toast.makeText(context, "JSON загружен", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Нет данных для офлайн-доступа", Toast.LENGTH_SHORT).show()
        }
    }

}

