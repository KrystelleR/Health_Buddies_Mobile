package com.varsitycollege.xbcad.healthbuddies

import ConfirmationDialogFragment
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Store : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val storeItems = listOf(
            StoreItem("Item 1", "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2FColorful_wave.jpeg?alt=media&token=2ddc4a03-da0c-4d42-9556-562183477fc7", "$10.99"),
            StoreItem("Item 2", "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2FRainbow_Colorful_for_free.jpeg?alt=media&token=4cdc6690-3cb8-48f4-8814-1c846860d5ac", "$15.49"),
            StoreItem("Item 3", "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2Fpink-blue-pink-and-blue-background.jpg?alt=media&token=a2a39869-d5d1-4ae1-8a84-867dc407fe45", "$8.99"),
            StoreItem("Item 4", "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2Fwavy-wallpaper.jpg?alt=media&token=c6e5321b-0fa0-4a99-a62a-32412c035adf", "$12.99")
        )


        val adapter = StoreAdapter(storeItems) { item ->
            // Handle the click event here
            showConfirmationDialog(item)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun showConfirmationDialog(item: StoreItem) {
        val dialogFragment = ConfirmationDialogFragment()
        dialogFragment.show(supportFragmentManager, "ConfirmationDialog")
    }

}
