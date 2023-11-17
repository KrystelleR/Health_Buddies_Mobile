package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foodcard = findViewById<CardView>(R.id.foodcardbtn)
        val exercisecard = findViewById<CardView>(R.id.exercisecardbtn)
        val sleepcard = findViewById<CardView>(R.id.sleepcardbtn)


        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        // Set the menu to the NavigationView
        navView.menu.clear()
        navView.inflateMenu(R.menu.nav_header_menu)

        val drawerLayout: DrawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the color of the hamburger icon
        toggle.drawerArrowDrawable?.setColorFilter(
            resources.getColor(R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        foodcard.setOnClickListener {
            val intent = Intent(this, melodiespage::class.java)
            startActivity(intent)
        }

        exercisecard.setOnClickListener {
            val intent = Intent(this, fitnesspage::class.java)
            startActivity(intent)
        }

        sleepcard.setOnClickListener {
            val intent = Intent(this, sleeppage::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home_item -> {
                // Handle item1 click
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.food_item -> {
                val intent = Intent(this, sleeppage::class.java)
                startActivity(intent)
            }
            R.id.exercise_item -> {
                val intent = Intent(this, fitnesspage::class.java)
                startActivity(intent)
            }
            R.id.sleep_item -> {
                // Handle item5 click
                val intent = Intent(this, sleeppage::class.java)
                startActivity(intent)
            }
            R.id.settings_item -> {
                // Handle item5 click
                val intent = Intent(this, settingspage::class.java)
                startActivity(intent)
            }
            R.id.logout_item -> {

                val confirmDialog = AlertDialog.Builder(this)
                confirmDialog.setTitle("Confirmation")
                confirmDialog.setMessage("Are you sure you want to Log Out?")

                confirmDialog.setPositiveButton("Yes") { dialog, which ->
                    // Save changes and display success message
                    val successDialog = AlertDialog.Builder(this)
                    successDialog.setTitle("Success")
                    successDialog.setMessage("Successfully Logged Out.")
                    successDialog.setPositiveButton("OK") { _, _ ->

                        Firebase.auth.signOut()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)

                    }
                    successDialog.show()
                }

                confirmDialog.setNegativeButton("No") { dialog, which ->
                    // Perform any action if needed when the user cancels the operation
                }

                confirmDialog.show()

            }
            // Handle other menu items here

            // Return true to indicate that the item selection has been handled
            else -> return true
        }

        // Return false to indicate that the item selection has not been handled
        return false
    }
}