package com.varsitycollege.xbcad.healthbuddies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class PointsFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var leaderboardListView: ListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.points_leaderboard, container, false)
        leaderboardListView = view.findViewById<ListView>(R.id.leaderboardListView)

        // Initialize Firebase components
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            databaseReference = FirebaseDatabase.getInstance().reference.child("Users")

            // Fetch leaderboard data
            fetchLeaderboardData()
        }
        return view
    }


    private fun fetchLeaderboardData() {
        val query: Query = databaseReference.orderByChild("userCurrency")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val leaderboardEntries = mutableListOf<LeaderboardEntry>()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // Add log statements to see the data
                        Log.d("Leaderboard", "User: $user")

                        Log.d("Leaderboard", "UserName: ${user.username ?: "null"}, UserCurrency: ${user.userCurrency}")

                        leaderboardEntries.add(
                            LeaderboardEntry(user.username ?: "N/A", user.userCurrency)
                        )
                    }
                }

                // Sort the entries based on userCurrency in descending order
                leaderboardEntries.sortByDescending { it.points }

                val adapter = LeaderboardAdapter(requireContext(), leaderboardEntries)
                leaderboardListView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("Leaderboard", "Error fetching leaderboard data: ${databaseError.message}")
            }
        })
    }

}

