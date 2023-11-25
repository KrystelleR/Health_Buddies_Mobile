package com.varsitycollege.xbcad.healthbuddies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class StepsLeaderBoard : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var stepsLeaderboardListView: ListView
    private lateinit var userEntries: List<UserEntry>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.steps_leaderboard, container, false)
        stepsLeaderboardListView = view.findViewById<ListView>(R.id.leaderboardListView)

        // Initialize Firebase components
        val firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference.child("UserSteps")

        // Fetch user data
        fetchUserData()

        // Fetch steps leaderboard data
        fetchStepsLeaderboardData()

        return view
    }

    private fun fetchUserData() {
        val usersReference = FirebaseDatabase.getInstance().reference.child("Users")

        usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userEntries = mutableListOf()

                for (userSnapshot in dataSnapshot.children) {
                    val uid = userSnapshot.key
                    val username = userSnapshot.child("username").getValue(String::class.java)

                    if (uid != null && username != null) {
                        Log.d("StepsLeaderboard", "User UID: $uid, Username: $username")

                        (userEntries as MutableList<UserEntry>).add(UserEntry(uid, username))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("StepsLeaderboard", "Error fetching user data: ${databaseError.message}")
            }
        })
    }

    private fun fetchStepsLeaderboardData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val stepsLeaderboardEntries = mutableListOf<StepsLeaderboardEntry>()

                for (userSnapshot in dataSnapshot.children) {
                    val userUid = userSnapshot.key

                    // Initialize highest steps for the user
                    var highestSteps = 0

                    for (stepSnapshot in userSnapshot.children) {
                        val stepsValue = stepSnapshot.value

                        val steps = when (stepsValue) {
                            is Long -> stepsValue.toInt()
                            is String -> stepsValue.toIntOrNull() ?: 0
                            else -> 0
                        }

                        // Update highest steps if the current steps are greater
                        if (steps > highestSteps) {
                            highestSteps = steps
                        }
                    }

                    // Find the corresponding username for the userUid
                    val username = userEntries.find { it.uid == userUid }?.username ?: "N/A"

                    // Add log statements to see the data
                    Log.d("StepsLeaderboard", "User UID: $userUid, Username: $username, Highest Steps: $highestSteps")

                    stepsLeaderboardEntries.add(
                        StepsLeaderboardEntry(userUid ?: "N/A", username, highestSteps)
                    )
                }

                // Sort the entries based on highestSteps in descending order
                stepsLeaderboardEntries.sortByDescending { it.highestSteps }

                val adapter = StepsLeaderboardAdapter(requireContext(), stepsLeaderboardEntries)
                stepsLeaderboardListView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e("StepsLeaderboard", "Error fetching steps leaderboard data: ${databaseError.message}")
            }
        })
    }
}

data class UserEntry(val uid: String, val username: String)
data class StepsLeaderboardEntry(val userUid: String, val username: String, val highestSteps: Int)
