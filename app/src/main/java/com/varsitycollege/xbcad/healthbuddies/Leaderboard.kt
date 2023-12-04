package com.varsitycollege.xbcad.healthbuddies

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class User(
    val uid: String,
    val username: String,  // Update the field name here
    val userCurrency: Int
) {
    constructor() : this("", "", 0)
}
class Leaderboard : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var leaderboardListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_leaderboard)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val pagerAdapter = LeaderBoardPageAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)



    }



}