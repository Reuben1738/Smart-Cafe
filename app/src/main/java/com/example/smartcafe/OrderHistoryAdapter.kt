package com.example.smartcafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvOrderItems: TextView = itemView.findViewById(R.id.tvOrderItems)
        val tvOrderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.tvOrderId.text = "Order #${order.id}"
        holder.tvOrderItems.text = order.itemsSummary
        holder.tvOrderTotal.text = "Total: £${String.format("%.2f", order.totalPrice)}"
        holder.tvOrderStatus.text = "Status: ${order.status}"

        // Format the timestamp to a readable date
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        holder.tvOrderDate.text = sdf.format(Date(order.timestamp))

        // Change status color based on the status
        when (order.status) {
            "Pending" -> holder.tvOrderStatus.setTextColor(0xFFFF9800.toInt()) // Orange
            "Preparing" -> holder.tvOrderStatus.setTextColor(0xFF2196F3.toInt()) // Blue
            "Ready" -> holder.tvOrderStatus.setTextColor(0xFF4CAF50.toInt()) // Green
        }
    }

    override fun getItemCount() = orderList.size
}