package com.example.smartcafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.MenuItem

class MenuAdapter(private val menuList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
        val ivItemImage: ImageView = itemView.findViewById(R.id.ivItemImage)
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


        holder.ivItemImage.setImageResource(item.imageResId)

        holder.btnAddToCart.setOnClickListener {
            CartManager.addToCart(item)
            Toast.makeText(holder.itemView.context, "${item.name} added to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = menuList.size
}