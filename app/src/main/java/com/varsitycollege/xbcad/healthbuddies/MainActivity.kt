package com.varsitycollege.xbcad.healthbuddies

import android.content.ContentValues
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private val baseUrl = "https://api.quotable.io/"
    private lateinit var leaderboardbtn: Button

    var myprofileimg: Int =0
    var mysetDetails: Boolean = false

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


        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val quoteService = retrofit.create(QuoteService::class.java)

        val call = quoteService.getRandomQuote()

        call.enqueue(object : Callback<data.Quote> {
            override fun onResponse(call: Call<data.Quote>, response: Response<data.Quote>) {
                if (response.isSuccessful) {
                    val quote = response.body()
                    if (quote != null) {
                        // Do something with the quote
                        val quoteText = quote.content
                        val author = quote.author

                        val dailyQuoteTxt = findViewById<TextView>(R.id.dailyQuoteTxt)
                        dailyQuoteTxt.text = "\"$quoteText\"" + " -$author"
                    }
                }
            }

            override fun onFailure(call: Call<data.Quote>, t: Throwable) {
                // Handle failure
            }
        })

   //navbar header code
        val user = FirebaseAuth.getInstance().currentUser
        // Set email value to TextView in nav_header.xml
        val headerView = navView.getHeaderView(0)
        val emailTextView: TextView = headerView.findViewById(R.id.emailtxt)
        emailTextView.text = user?.email
        // Set name value to TextView in nav_header.xml
        val nameView: TextView = headerView.findViewById(R.id.user_name)
        nameView.text = user?.displayName

        val profileView: de.hdodenhof.circleimageview.CircleImageView = headerView.findViewById(R.id.profile_image)
        val profileimg = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)

        val database = FirebaseDatabase.getInstance()
        // Getting user details from db
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid

            // Now you can use uid in your button click listener
            val bannerItemsButton: Button = findViewById(R.id.button2)

            bannerItemsButton.setOnClickListener {
                val dialogFragment = BannerItemsDialogFragment(userUid, this@MainActivity)
                dialogFragment.show(supportFragmentManager, "BannerItemsDialog")
            }


            // Reference to the user's data in the Realtime Database
            val userRef = database.getReference("Users").child(userUid) // Corrected line

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val userDetails = dataSnapshot.getValue(data.UserDetails::class.java)

                        // Now you can use the userDetails object as needed
                        if (userDetails != null) {
                            myprofileimg = userDetails.profileImage
                            mysetDetails = userDetails.setDetails

                            // Set the TextView values here
                            profileimg.setImageResource(myprofileimg)
                            profileView.setImageResource(myprofileimg)

                        }
                    } else {
                        Log.d(ContentValues.TAG, "User details do not exist")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        ContentValues.TAG,
                        "Error reading user details from the database",
                        databaseError.toException()
                    )
                }
            })
        }

        foodcard.setOnClickListener {
            val intent = Intent(this, Nutrition::class.java)
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

        leaderboardbtn = findViewById(R.id.leaderboardbtn)

        leaderboardbtn.setOnClickListener {
            // When the button is clicked, navigate to the LeaderboardActivity
            val intent = Intent(this, Leaderboard::class.java)
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
                val intent = Intent(this, Nutrition::class.java)
                startActivity(intent)
            }
            R.id.water_item -> {
                val intent = Intent(this, Nutrition::class.java)
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
            R.id.store_item -> {
                // Handle item6 click
                val intent = Intent(this, Store::class.java)
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

     fun onImageClick(imageUrl: String) {
        val imageView: ImageView = findViewById(R.id.imageView)

        // Load and display the image using Glide or your preferred library
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Make the ImageView visible
        imageView.visibility = View.VISIBLE
    }

}