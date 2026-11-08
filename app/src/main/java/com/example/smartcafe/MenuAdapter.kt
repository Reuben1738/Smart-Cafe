package com.example.smartcafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.MenuItem

class MenuAdapter(private val menuList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        // Find the new button we just added
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.tvName.text = item.name
        holder.tvDesc.text = item.description
        holder.tvPrice.text = "£${item.price}"

        // Set up the click listener for the Add button
        holder.btnAddToCart.setOnClickListener {
            // Add the specific item to our CartManager
            CartManager.addToCart(item)

            // Show a confirmation message to the user
            Toast.makeText(holder.itemView.context, "${item.name} added to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = menuList.size
}