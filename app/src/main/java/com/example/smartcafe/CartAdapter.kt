package com.example.smartcafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.MenuItem

class CartAdapter(
    private val cartItems: MutableMap<MenuItem, Int>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartList = cartItems.keys.toList()

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemoveItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        val quantity = cartItems[item] ?: 0

        holder.tvName.text = item.name
        holder.tvPrice.text = "£${String.format("%.2f", item.price * quantity)}"
        holder.tvQuantity.text = quantity.toString()

        holder.btnPlus.setOnClickListener {
            CartManager.addToCart(item)
            notifyItemChanged(position)
            onCartUpdated()
        }

        holder.btnMinus.setOnClickListener {
            CartManager.removeFromCart(item)
            if (CartManager.cartItems[item] == null) {
                notifyItemRemoved(position)
                notifyDataSetChanged()
            } else {
                notifyItemChanged(position)
            }
            onCartUpdated()
        }

        holder.btnRemove.setOnClickListener {
            CartManager.deleteItem(item)
            notifyItemRemoved(position)
            notifyDataSetChanged()
            onCartUpdated()
        }
    }

    override fun getItemCount() = cartList.size
}