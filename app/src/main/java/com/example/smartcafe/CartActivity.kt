package com.example.smartcafe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Import for Coroutines
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcafe.data.local.AppDatabase // Import AppDatabase
import com.example.smartcafe.data.model.Order // Import Order model
import com.example.smartcafe.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth // Import FirebaseAuth
import kotlinx.coroutines.launch // Import launch

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        // passes the list from CartManager to the Adapter
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