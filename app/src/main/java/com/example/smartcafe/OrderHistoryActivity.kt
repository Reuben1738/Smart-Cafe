package com.example.smartcafe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcafe.data.local.AppDatabase
import com.example.smartcafe.data.model.Order
import com.example.smartcafe.databinding.ActivityOrderHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvOrderHistory.layoutManager = LinearLayoutManager(this)

        // Load orders from the database for the current user
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@OrderHistoryActivity)
            val orders = db.orderDao().getOrdersByUser(userEmail)

            binding.rvOrderHistory.adapter = OrderHistoryAdapter(orders)
        }
    }
}