package com.varsitycollege.xbcad.healthbuddies

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Store : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val storeItems = listOf(
            StoreItem("Item 1", R.drawable.bannerbg, "$10.99"),
            StoreItem("Item 2", R.drawable.bannerbg, "$15.49"),
            StoreItem("Item 3", R.drawable.bannerbg, "$8.99"),
            StoreItem("Item 4", R.drawable.bannerbg, "$12.99")
        )

        recyclerView.adapter = StoreAdapter(storeItems)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        /*val bannerImageView = findViewById<ImageView>(R.id.itemImage) // Replace with your banner ImageView

        bannerImageView.setOnClickListener {
            val dialogFragment = ConfDialog()
            dialogFragment.show(supportFragmentManager, "ConfirmationDialog")
        }*/
    }
}
