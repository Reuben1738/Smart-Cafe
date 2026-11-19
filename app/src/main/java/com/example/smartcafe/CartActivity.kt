package com.example.smartcafe

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcafe.data.local.AppDatabase
import com.example.smartcafe.data.model.Order
import com.example.smartcafe.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Security check: if not logged in, go to Login
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupRecyclerView()
        updateTotalPrice()

        // Checkout Button Logic (with Simulated Payment)
        binding.btnCheckout.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                showPaymentDialog()
            }
        }
    }

    private fun showPaymentDialog() {
        val totalPrice = CartManager.getTotalPrice()

        AlertDialog.Builder(this)
            .setTitle("Payment")
            .setMessage("Your total is £${String.format("%.2f", totalPrice)}.\n\nDo you want to proceed with the payment?")
            .setPositiveButton("Pay Now") { dialog, _ ->
                dialog.dismiss()
                processOrder()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun processOrder() {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"

        // Create a summary of items from the Map (Quantity x Name)
        val itemsSummary = CartManager.cartItems.entries.joinToString(", ") {
            "${it.value}x ${it.key.name}"
        }

        val newOrder = Order(
            userEmail = userEmail,
            itemsSummary = itemsSummary,
            totalPrice = CartManager.getTotalPrice(),
            status = "Pending"
        )

        // Save to Room Database
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@CartActivity)
            db.orderDao().insertOrder(newOrder)

            Toast.makeText(this@CartActivity, "Payment Successful! Order Placed.", Toast.LENGTH_LONG).show()
            CartManager.clearCart()
            finish()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(CartManager.cartItems) {
            updateTotalPrice()
        }
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter
    }

    private fun updateTotalPrice() {
        val total = CartManager.getTotalPrice()
        binding.tvTotalPrice.text = "Total: £${String.format("%.2f", total)}"
    }
}