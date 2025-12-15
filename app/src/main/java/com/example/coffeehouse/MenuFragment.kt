package com.example.coffeehouse

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

        val drinksCall = api.getMenuDrinks()
        val dessertsCall = api.getMenuDesserts()

        drinksCall.enqueue(object : Callback<List<MenuItem>> {
            override fun onResponse(call: Call<List<MenuItem>>, drinksResp: Response<List<MenuItem>>) {

                dessertsCall.enqueue(object : Callback<List<MenuItem>> {
                    override fun onResponse(call: Call<List<MenuItem>>, dessertsResp: Response<List<MenuItem>>) {

                        if (!isAdded) return

                        val drinks = drinksResp.body() ?: emptyList()
                        val desserts = dessertsResp.body() ?: emptyList()

                        val fullMenu = drinks + desserts

                        adapter.setItems(fullMenu)
                        saveJSON(fullMenu)
                    }

                    override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                        handleFailure()
                    }
                })
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                handleFailure()
            }
        })
    }

    private fun handleFailure() {
        if (!isAdded) return

        val safeContext = context ?: return

        if (safeContext.isNetworkAvailable()) {
            Toast.makeText(safeContext, R.string.menu_load_error, Toast.LENGTH_SHORT).show()
            loadMenuFromJSON()
        } else {
            MaterialAlertDialogBuilder(safeContext, R.style.CoffeeAlertDialog)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_message)
                .setPositiveButton(R.string.Okay, null)
                .show()
            loadMenuFromJSON()
        }
    }

    private fun saveJSON(menuList: List<MenuItem>){
        val gson = Gson()
        val jsonString = gson.toJson(menuList)
        val file = File(requireContext().filesDir, "menu.json")
        file.writeText(jsonString)
        Toast.makeText(context, R.string.json_saved, Toast.LENGTH_SHORT).show()
    }

    private fun loadMenuFromJSON() {
        val file = File(requireContext().filesDir, "menu.json")
        if (file.exists()) {
            val jsonString = file.readText()
            val gson = Gson()
            val menuList = gson.fromJson(jsonString, Array<MenuItem>::class.java).toList()
            adapter.setItems(menuList)
            Toast.makeText(context, R.string.json_loaded, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,R.string.no_offline_data, Toast.LENGTH_SHORT).show()
        }
    }

}

