package com.varsitycollege.xbcad.healthbuddies

import ConfirmationDialogFragment
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Store : AppCompatActivity(), ConfirmationDialogFragment.ConfirmationDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val storeItems = listOf(
            StoreItem(
                "Item 1",
                "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2FColorful_wave.jpeg?alt=media&token=2ddc4a03-da0c-4d42-9556-562183477fc7",
                "$10.99"
            ),
            StoreItem(
                "Item 2",
                "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2FRainbow_Colorful_for_free.jpeg?alt=media&token=4cdc6690-3cb8-48f4-8814-1c846860d5ac",
                "$15.49"
            ),
            StoreItem(
                "Item 3",
                "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2Fpink-blue-pink-and-blue-background.jpg?alt=media&token=a2a39869-d5d1-4ae1-8a84-867dc407fe45",
                "$8.99"
            ),
            StoreItem(
                "Item 4",
                "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2Fwavy-wallpaper.jpg?alt=media&token=c6e5321b-0fa0-4a99-a62a-32412c035adf",
                "$12.99"
            )
        )

        val adapter = StoreAdapter(storeItems) { item ->
            showConfirmationDialog(item)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        fetchUserData();

    }

    private fun showConfirmationDialog(item: StoreItem) {
        val dialogFragment = ConfirmationDialogFragment()
        dialogFragment.show(supportFragmentManager, "ConfirmationDialog")
    }

    override fun onConfirmClicked() {
        // Handle the confirmation logic here, e.g., initiate the purchase
        Toast.makeText(this, "Item purchased!", Toast.LENGTH_SHORT).show()
    }

    override fun onCancelClicked() {
        // Handle the cancellation logic here
        Toast.makeText(this, "Purchase canceled", Toast.LENGTH_SHORT).show()
    }

    private fun fetchUserData() {
        val database = FirebaseDatabase.getInstance()
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            val userUid = currentUser.uid
            val userRef = database.getReference("Users").child(userUid)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userDetails = dataSnapshot.getValue(data.UserDetails::class.java)

                        if (userDetails != null) {
                            val mycurrency = userDetails.userCurrency

                            Toast.makeText(this@Store, "my currency: ${mycurrency}", Toast.LENGTH_SHORT).show()

                            val currency: TextView = findViewById(R.id.userCurrencyTxt)
                            currency.text = mycurrency.toString()
                        }
                    } else {
                        Log.d(ContentValues.TAG, "User details do not exist")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "Error reading user details from the database", databaseError.toException())
                }
            })
        }
    }
    private fun fetchStoreItems() {
        val database = FirebaseDatabase.getInstance()
        val storeRef = database.getReference("Store")

        storeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val storeItemsList = mutableListOf<StoreItem>()

                    for (itemSnapshot in dataSnapshot.children) {
                        val imageUrl = itemSnapshot.child("ImageUrl").getValue(String::class.java)
                        val name = itemSnapshot.child("Name").getValue(String::class.java)
                        val points = itemSnapshot.child("Points").getValue(Long::class.java)

                        if (imageUrl != null && name != null && points != null) {
                            val storeItem = StoreItem(name, imageUrl, "$points Points")
                            storeItemsList.add(storeItem)
                        }
                    }

                    // Now you can use storeItemsList to populate your RecyclerView
                    val adapter = StoreAdapter(storeItemsList) { item ->
                        showConfirmationDialog(item)
                    }
                    //recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "Error reading store items from the database", databaseError.toException())
            }
        })
    }

}
