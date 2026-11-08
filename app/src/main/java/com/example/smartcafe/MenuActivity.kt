package com.example.smartcafe

import android.content.Intent
import android.os.Bundle
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
        val userNamePart = userEmail.substringBefore("@") // Gets "reuben" from "reuben@gmail.com"
        val formattedName = userNamePart.replaceFirstChar { it.uppercase() } // Makes it "Reuben"

        binding.tvWelcome.text = "Welcome, $formattedName"
        // ---------------------------------------------

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Go to Cart Screen
        binding.btnViewCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
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
                db.menuItemDao().insertMenuItem(MenuItem(name = "Latte", description = "Smooth espresso with steamed milk", price = 7.50))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Cappuccino", description = "Espresso with thick milk foam", price = 5.20))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Chocolate Cake", description = "Rich and moist chocolate cake", price = 9.00))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Croissant", description = "Butter croissant", price = 3.50))
                db.menuItemDao().insertMenuItem(MenuItem(name = "Iced Tea", description = "Refreshing lemon iced tea", price = 4.80))

                Toast.makeText(this@MenuActivity, "Menu Populated!", Toast.LENGTH_SHORT).show()
            }

            val menuItems: List<MenuItem> = db.menuItemDao().getAllMenuItems()
            menuAdapter = MenuAdapter(menuItems)
            binding.rvMenu.adapter = menuAdapter
        }
    }
}