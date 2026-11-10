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
                // MENU ITEMS WITH IMAGES
                db.menuItemDao().insertMenuItem(MenuItem(name = "Latte", description = "Smooth espresso with steamed milk", price = 7.50, imageResId = R.drawable.latte ))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Cappuccino", description = "Espresso with thick milk foam", price = 5.20, imageResId = R.drawable.cappuccino))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Chocolate Cake", description = "Rich and moist chocolate cake", price = 9.00, imageResId = R.drawable.chocolate_cake))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Croissant", description = "Freshly baked butter croissant", price = 3.50, imageResId = R.drawable.croissant))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Iced Tea", description = "Refreshing lemon iced tea", price = 4.80, imageResId = R.drawable.iced_tea))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Flat White", description = "Double espresso with microfoam", price = 6.80, imageResId = R.drawable.flat_white))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Mocha", description = "Espresso, chocolate, and steamed milk", price = 7.20, imageResId = R.drawable.mocha))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Americano", description = "Espresso diluted with hot water", price = 4.50, imageResId = R.drawable.americano))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Blueberry Muffin", description = "Fluffy muffin packed with blueberries", price = 4.20, imageResId = R.drawable.blueberry_muffin))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Cinnamon Roll", description = "Soft, sweet, and glazed", price = 5.50, imageResId = R.drawable.cinnamon_roll))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Hot Chocolate", description = "Rich, creamy, and topped with marshmallows", price = 5.00, imageResId = R.drawable.hot_chocolate))
                db.menuItemDao().insertMenuItem(MenuItem(name = "French Toast", description = "Thick sliced brioche with syrup and butter", price = 8.50, imageResId = R.drawable.french_toast))

                Toast.makeText(this@MenuActivity, "This is Menu!", Toast.LENGTH_SHORT).show()
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