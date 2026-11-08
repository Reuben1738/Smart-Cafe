package com.example.smartcafe

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcafe.databinding.ActivityCartBinding

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

                Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show()
                CartManager.clearCart() // Empty the cart
                finish() // Go back to Menu
            }
        }
    }

    private fun setupRecyclerView() {
        // We pass the list from CartManager to the Adapter
        cartAdapter = CartAdapter(CartManager.cartItems) {
            // This is a callback: when an item is removed, we refresh the total price
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