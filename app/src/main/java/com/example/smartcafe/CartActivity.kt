package com.example.smartcafe

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

        // Checkout Button Logic
        binding.btnCheckout.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                // 1. Get the current user's email from Firebase
                val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown"

                // 2. Create a summary of items
                val itemsSummary = CartManager.cartItems.groupingBy { it.name }.eachCount()
                    .map { "${it.value}x ${it.key}" }
                    .joinToString(", ")

                // 3. Create the Order object
                val newOrder = Order(
                    userEmail = userEmail,
                    itemsSummary = itemsSummary,
                    totalPrice = CartManager.getTotalPrice(),
                    status = "Pending"
                )

                // 4. Save to Room Database
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@CartActivity)
                    db.orderDao().insertOrder(newOrder)

                    // 5. Show success and clear the cart
                    Toast.makeText(this@CartActivity, "Order Placed Successfully!", Toast.LENGTH_LONG).show()
                    CartManager.clearCart()
                    finish()
                }
            }
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