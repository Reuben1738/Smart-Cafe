package com.example.smartcafe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryAdapter(
    private val orderList: List<Order>,
    private val onSimulateAdminClick: (Order) -> Unit,
    private val onFeedbackClick: (Order) -> Unit // NEW Callback
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvOrderItems: TextView = itemView.findViewById(R.id.tvOrderItems)
        val tvOrderTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val btnSimulateAdmin: Button = itemView.findViewById(R.id.btnSimulateAdmin)
        val btnLeaveFeedback: Button = itemView.findViewById(R.id.btnLeaveFeedback) // NEW
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

        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        holder.tvOrderDate.text = sdf.format(Date(order.timestamp))

        when (order.status) {
            "Pending" -> holder.tvOrderStatus.setTextColor(0xFFFF9800.toInt())
            "Preparing" -> holder.tvOrderStatus.setTextColor(0xFF2196F3.toInt())
            "Ready for Collection" -> holder.tvOrderStatus.setTextColor(0xFF4CAF50.toInt())
            "Collected" -> {
                holder.tvOrderStatus.setTextColor(0xFF757575.toInt())
                holder.btnSimulateAdmin.isEnabled = false
                holder.btnSimulateAdmin.text = "Completed"
            }
        }

        // Enable feedback ONLY if the order is collected
        if (order.status == "Collected") {
            holder.btnLeaveFeedback.isEnabled = true
            holder.btnLeaveFeedback.alpha = 1.0f
            // Orange when active
            holder.btnLeaveFeedback.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFFF9800.toInt())
        } else {
            holder.btnLeaveFeedback.isEnabled = false
            holder.btnLeaveFeedback.alpha = 0.5f
            //  Grey when disabled
            holder.btnLeaveFeedback.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFBDBDBD.toInt())
        }

        holder.btnSimulateAdmin.setOnClickListener {
            onSimulateAdminClick(order)
        }

        holder.btnLeaveFeedback.setOnClickListener {
            onFeedbackClick(order)
        }
    }

    override fun getItemCount() = orderList.size
}