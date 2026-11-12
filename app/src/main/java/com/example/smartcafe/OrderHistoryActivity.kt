package com.example.smartcafe

import android.app.AlertDialog
import android.content.Intent // <-- ADDED IMPORT
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcafe.data.local.AppDatabase
import com.example.smartcafe.data.model.Feedback
import com.example.smartcafe.data.model.Order
import com.example.smartcafe.databinding.ActivityOrderHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Security check: if not logged in, go to Login
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        db = AppDatabase.getDatabase(this)
        binding.rvOrderHistory.layoutManager = LinearLayoutManager(this)

        loadOrders()
    }

    private fun loadOrders() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        lifecycleScope.launch {
            val orders = db.orderDao().getOrdersByUser(userEmail)

            binding.rvOrderHistory.adapter = OrderHistoryAdapter(
                orders,
                onSimulateAdminClick = { order -> simulateAdminUpdate(order) },
                onFeedbackClick = { order -> showFeedbackDialog(order) }
            )
        }
    }

    private fun simulateAdminUpdate(order: Order) {
        val nextStatus = when (order.status) {
            "Pending" -> "Preparing"
            "Preparing" -> "Ready for Collection"
            "Ready for Collection" -> "Collected"
            else -> "Collected"
        }

        if (order.status == "Collected") {
            Toast.makeText(this, "This order is already collected.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            db.orderDao().updateOrderStatus(order.id, nextStatus)
            Toast.makeText(this@OrderHistoryActivity, "Status updated to: $nextStatus", Toast.LENGTH_SHORT).show()
            loadOrders() // Refresh the list
        }
    }

    private fun showFeedbackDialog(order: Order) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Rate Order #${order.id}")

        // Create a RatingBar with the standard star style
        val ratingBar = RatingBar(this, null, android.R.attr.ratingBarStyle)
        ratingBar.numStars = 5
        ratingBar.stepSize = 1f
        ratingBar.rating = 5f

        // Center the stars in the dialog
        val layout = android.widget.LinearLayout(this)
        layout.gravity = android.view.Gravity.CENTER
        val padding = (16 * resources.displayMetrics.density).toInt()
        layout.setPadding(padding, padding, padding, padding)
        layout.addView(ratingBar)

        builder.setView(layout)

        builder.setPositiveButton("Submit") { dialog, _ ->
            val rating = ratingBar.rating.toInt()
            submitFeedback(order, rating)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun submitFeedback(order: Order, rating: Int) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        lifecycleScope.launch {
            val feedback = Feedback(
                orderId = order.id,
                userEmail = userEmail,
                rating = rating
            )
            db.feedbackDao().insertFeedback(feedback)
            Toast.makeText(this@OrderHistoryActivity, "Thank you for your $rating-star feedback!", Toast.LENGTH_SHORT).show()
        }
    }
}