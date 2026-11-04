package com.example.smartcafe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartcafe.data.local.AppDatabase
import com.example.smartcafe.data.model.MenuItem
import com.example.smartcafe.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = AppDatabase.getDatabase(this)

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.tvWelcome.text = "Welcome, ${auth.currentUser?.email}"

        insertSampleData()

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun insertSampleData() {
        lifecycleScope.launch {
            val dao = db.menuItemDao()
            dao.insertMenuItem(MenuItem(name = "Latte", description = "Smooth espresso with steamed milk", price = 3.50))
            dao.insertMenuItem(MenuItem(name = "Cappuccino", description = "Espresso with Oat milk ", price = 3.20))
            dao.insertMenuItem(MenuItem(name = "Chocolate Cake", description = "Slice of Rich dark chocolate ", price = 4.50))
            dao.insertMenuItem(MenuItem(name = "Croissant", description = "Freshly baked croissant", price = 2.80))
        }
    }
}