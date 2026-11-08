package com.example.smartcafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.MenuItem

class CartAdapter(
    private val cartList: MutableList<MenuItem>,
    private val onItemRemoved: () -> Unit // Callback to update total price
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemoveItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        holder.tvName.text = item.name
        holder.tvPrice.text = "£${item.price}"

        // Handle item removal
        holder.btnRemove.setOnClickListener {
            CartManager.removeFromCart(item)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            onItemRemoved() // Trigger the callback to update the total price
        }
    }

    override fun getItemCount() = cartList.size
}