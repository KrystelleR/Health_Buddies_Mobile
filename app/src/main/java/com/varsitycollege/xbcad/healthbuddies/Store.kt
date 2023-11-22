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
        val selectedItem = (recyclerView.adapter as? StoreAdapter)?.getSelectedItem()

        if (selectedItem != null) {
            val selectedPoints = selectedItem.points

            // Reference to the user's currency node
            val userCurrencyRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUid).child("userCurrency")

            userCurrencyRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentCurrency = mutableData.getValue(Long::class.java) ?: 0

                    // Ensure the user has enough currency to make the purchase
                    return if (currentCurrency >= selectedPoints) {
                        mutableData.value = currentCurrency - selectedPoints
                        Transaction.success(mutableData)
                    } else {
                        Transaction.abort()
                    }
                }

                override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?) {
                    if (committed) {
                        // Purchase successful
                        Toast.makeText(this@Store, "Item purchased!", Toast.LENGTH_SHORT).show()

                        // Update the PurchasedItems in the database
                        val userPurchasedItemsRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUid).child("PurchasedItems")
                        userPurchasedItemsRef.child(selectedItem.storeId).setValue(true)

                        // Refresh user currency display
                        fetchUserData()
                    } else {
                        // Insufficient currency
                        Toast.makeText(this@Store, "Purchase failed: Insufficient currency", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            // Handle the case where no item is selected
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

        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val storeItemList = ArrayList<StoreItem>()

                    for (storeSnapshot in snapshot.children) {
                        // Access properties directly from storeSnapshot
                        val name = storeSnapshot.child("Name").getValue(String::class.java)
                        val imageUrl = storeSnapshot.child("ImageUrl").getValue(String::class.java)
                        val points = storeSnapshot.child("Points").getValue(Long::class.java)
                        val storeId = storeSnapshot.key // Get the storeId from the snapshot

                        if (name != null && imageUrl != null && points != null && storeId != null) {
                            val storeItem = StoreItem(imageUrl, name, points, storeId)

                            // Check if the item is not already purchased
                            isItemPurchased(storeItem) { isPurchased ->
                                if (!isPurchased) {
                                    storeItemList.add(storeItem)
                                    // Update adapter data
                                    adapter.setItems(storeItemList)
                                    adapter.notifyDataSetChanged()
                                }

                                // Log the imageUrl for debugging
                                Log.d("FirebaseData", "ImageUrl: $imageUrl")
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("FirebaseData", "Error: ${error.message}")
            }
        })
    }



    private fun isItemPurchased(storeItem: StoreItem, callback: (Boolean) -> Unit) {
        // Check if the item is already purchased
        val userPurchasedItemsRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUid).child("PurchasedItems")
        userPurchasedItemsRef.child(storeItem.storeId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // If the snapshot exists, the item is already purchased
                callback(snapshot.exists())
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("FirebaseData", "Error: ${error.message}")
                callback(false) // Assume the item is not purchased in case of an error
            }
        })
    }
}



