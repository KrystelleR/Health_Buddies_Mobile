package com.varsitycollege.xbcad.healthbuddies

import ConfirmationDialogFragment
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.Transaction

class Store : AppCompatActivity(), ConfirmationDialogFragment.ConfirmationDialogListener {
    private lateinit var currentUserUid: String
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        currentUserUid = Firebase.auth.currentUser?.uid.orEmpty()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val adapter = StoreAdapter(ArrayList()) { item ->
            showConfirmationDialog(item)
        }
        recyclerView.adapter = adapter

        populateStoreItems(adapter)
        fetchUserData()
    }

    //region Confirmation Dialog code
    private fun showConfirmationDialog(item: StoreItem) {
        val dialogFragment = ConfirmationDialogFragment()
        dialogFragment.show(supportFragmentManager, "ConfirmationDialog")
    }

    override fun onConfirmClicked() {
        // Get the selected item from the adapter
        val selectedItem = (recyclerView.adapter as? StoreAdapter)?.getSelectedItem()

        if (selectedItem != null) {
            val selectedPoints = selectedItem.points

            // Fetch user currency
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUid)

            userRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val userDetails = mutableData.getValue(data.UserDetails::class.java)

                    if (userDetails != null) {
                        val currentCurrency = userDetails.userCurrency

                        if (currentCurrency >= selectedPoints) {
                            // Sufficient currency for the purchase
                            userDetails.userCurrency = (currentCurrency - selectedPoints).toInt()

                            // Update user currency in the database
                            mutableData.value = userDetails
                            return Transaction.success(mutableData)
                        } else {
                            // Insufficient currency
                            return Transaction.abort()
                        }
                    }

                    // User details not found
                    return Transaction.success(mutableData)
                }

                override fun onComplete(
                    databaseError: DatabaseError?,
                    committed: Boolean,
                    dataSnapshot: DataSnapshot?
                ) {
                    if (committed) {
                        // Purchase successful
                        Toast.makeText(this@Store, "Item purchased!", Toast.LENGTH_SHORT).show()
                        fetchUserData() // Refresh user currency display
                    } else {
                        // Purchase failed (e.g., insufficient currency)
                        Toast.makeText(this@Store, "Purchase failed: Insufficient currency", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            // Handle the case where no item is selected (this should not happen in your flow)
            Toast.makeText(this@Store, "No item selected", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCancelClicked() {
        // Handle the cancellation logic here
        Toast.makeText(this, "Purchase canceled", Toast.LENGTH_SHORT).show()
    }
    //endregion

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

    private fun populateStoreItems(adapter: StoreAdapter) {
        val mDatabase = FirebaseDatabase.getInstance().getReference("Store")

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val storeItemList = ArrayList<StoreItem>()

                    for (storeSnapshot in snapshot.children) {
                        // Access properties directly from storeSnapshot
                        val name = storeSnapshot.child("Name").getValue(String::class.java)
                        val imageUrl = storeSnapshot.child("ImageUrl").getValue(String::class.java)
                        val points = storeSnapshot.child("Points").getValue(Long::class.java)

                        if (name != null && imageUrl != null && points != null) {
                            val storeItem = StoreItem(imageUrl, name, points)
                            storeItemList.add(storeItem)

                            // Log the imageUrl for debugging
                            Log.d("FirebaseData", "ImageUrl: $imageUrl")
                        }
                    }

                    Log.d("FirebaseData", "StoreItemList: $storeItemList")

                    // Update adapter data
                    Log.d("FirebaseData", "StoreItemList size before update: ${storeItemList.size}")
                    adapter.setItems(storeItemList)

                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("FirebaseData", "Error: ${error.message}")
            }
        })
    }
}



