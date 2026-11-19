package com.example.smartcafe

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smartcafe.data.model.MenuItem

class MenuAdapter(private val menuList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvItemDesc)
        val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
        val ivItemImage: ImageView = itemView.findViewById(R.id.ivItemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.tvName.text = item.name
        holder.tvDesc.text = item.description
        holder.tvPrice.text = "£${item.price}"
        holder.ivItemImage.setImageResource(item.imageResId)

        holder.btnAddToCart.setOnClickListener {
            // Check if the item is a drink that needs customization
            val itemNameLower = item.name.lowercase()

            // Added "flat white" and "hot chocolate" and "chocolate cake" exclusion
            val isDrink = itemNameLower.contains("latte") ||
                    itemNameLower.contains("cappuccino") ||
                    itemNameLower.contains("mocha") ||
                    itemNameLower.contains("americano") ||
                    itemNameLower.contains("tea") ||
                    itemNameLower.contains("hot chocolate") ||
                    itemNameLower.contains("flat white")

            if (isDrink) {
                // Show dialog for drinks
                showCustomizationDialog(holder.itemView, item)
            } else {
                // Just add directly to cart for food (Croissant, Cake, Toast)
                CartManager.addToCart(item)
                Toast.makeText(holder.itemView.context, "${item.name} added to cart!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCustomizationDialog(view: View, originalItem: MenuItem) {
        val milkOptions = arrayOf("Whole Milk", "Oat Milk", "Soy Milk", "No Milk")
        val sugarOptions = arrayOf("No Sugar", "1 Sugar", "2 Sugars")

        // Simple Dialog to choose Milk and Sugar
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Customize ${originalItem.name}")

        // Create a layout for the dialog
        val dialogLayout = android.widget.LinearLayout(view.context)
        dialogLayout.orientation = android.widget.LinearLayout.VERTICAL
        val padding = (16 * view.resources.displayMetrics.density).toInt()
        dialogLayout.setPadding(padding, padding, padding, padding)

        // Milk Spinner
        val milkSpinner = android.widget.Spinner(view.context)
        milkSpinner.adapter = android.widget.ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, milkOptions)
        dialogLayout.addView(milkSpinner)

        // Sugar Spinner
        val sugarSpinner = android.widget.Spinner(view.context)
        sugarSpinner.adapter = android.widget.ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, sugarOptions)
        dialogLayout.addView(sugarSpinner)

        builder.setView(dialogLayout)

        builder.setPositiveButton("Add to Cart") { dialog, _ ->
            val selectedMilk = milkSpinner.selectedItem.toString()
            val selectedSugar = sugarSpinner.selectedItem.toString()

            // Create a modified item name to reflect choices (e.g., "Latte (Oat Milk, No Sugar)")
            val customizedName = "${originalItem.name} ($selectedMilk, $selectedSugar)"

            // Create a new MenuItem with the customized name
            val customizedItem = MenuItem(
                id = originalItem.id,
                name = customizedName,
                description = originalItem.description,
                price = originalItem.price,
                imageResId = originalItem.imageResId
            )

            CartManager.addToCart(customizedItem)
            Toast.makeText(view.context, "$customizedName added to cart!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun getItemCount() = menuList.size
}