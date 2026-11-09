package com.example.smartcafe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartcafe.data.local.AppDatabase
import com.example.smartcafe.data.model.MenuItem
import com.example.smartcafe.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: AppDatabase
    private lateinit var menuAdapter: MenuAdapter
    private var allMenuItems: List<MenuItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = AppDatabase.getDatabase(this)

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userEmail = auth.currentUser?.email ?: "User"
        val userNamePart = userEmail.substringBefore("@")
        val formattedName = userNamePart.replaceFirstChar { it.uppercase() }
        binding.tvWelcome.text = "Welcome, $formattedName"

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnViewCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.btnOrderHistory.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        setupRecyclerView()
        checkAndSeedDatabase()
    }

    private fun setupRecyclerView() {
        menuAdapter = MenuAdapter(emptyList())
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = menuAdapter
    }

    private fun checkAndSeedDatabase() {
        lifecycleScope.launch {
            val existingItems: List<MenuItem> = db.menuItemDao().getAllMenuItems()

            if (existingItems.isEmpty()) {
                // --- 12 MENU ITEMS ---
                db.menuItemDao().insertMenuItem(MenuItem(name = "Latte", description = "Smooth espresso with steamed milk", price = 7.50))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Cappuccino", description = "Espresso with milk", price = 5.20))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Chocolate Cake", description = "Rich and moist chocolate cake", price = 9.00))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Croissant", description = "Freshly baked butter croissant", price = 3.50))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Iced Tea", description = "Lemon iced tea", price = 4.80))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Flat White", description = "Double espresso with foam", price = 6.80))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Mocha", description = "Espresso, chocolate, and steamed milk", price = 7.20))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Americano", description = "Espresso with hot water", price = 4.50))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Blueberry Muffin", description = "Fluffy muffin packed with blueberries", price = 4.20))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Cinnamon Roll", description = "Soft, sweet, and glazed", price = 5.50))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Hot Chocolate", description = "Rich, creamy, and hot chocolate", price = 5.00))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Avocado Toast", description = "Avocado on toast", price = 6.50))

                Toast.makeText(this@MenuActivity, "Menu Populated!", Toast.LENGTH_SHORT).show()
            }

            allMenuItems = db.menuItemDao().getAllMenuItems()
            menuAdapter = MenuAdapter(allMenuItems)
            binding.rvMenu.adapter = menuAdapter
            setupSearch()
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filteredList = allMenuItems.filter {
                    it.name.lowercase().contains(query) || it.description.lowercase().contains(query)
                }
                menuAdapter = MenuAdapter(filteredList)
                binding.rvMenu.adapter = menuAdapter
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}