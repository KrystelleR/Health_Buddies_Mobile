package com.varsitycollege.xbcad.healthbuddies

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
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
import java.text.SimpleDateFormat
import java.util.*
import com.varsitycollege.xbcad.healthbuddies.data

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private val baseUrl = "https://api.quotable.io/"
    private lateinit var leaderboardbtn: Button

    private lateinit var database: FirebaseDatabase
    private var userUid: String? = null

    var myprofileimg: String = ""
    var mysetDetails: Boolean = false
    var mybanner: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val actionBar = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false) // Hide the default title
        actionBar?.setCustomView(R.layout.action_bar_custom_layout)


        database = FirebaseDatabase.getInstance()
        // Getting user details from db
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid


            val userRef = database.getReference("Users").child(userUid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        var UserDetails = dataSnapshot.getValue(data.UserDetails::class.java)
                        if (UserDetails != null) {
                            if(UserDetails.setDetails == false){
                                Toast.makeText(this@MainActivity, "Let's update your user settings before beginning", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@MainActivity, settingspage::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

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

            // Reference to the user's data in the Realtime Database
            val userLoggedRef = database.getReference("LastLoggedIn").child(userUid)
            val userStepsRef = database.getReference("UserSteps").child(userUid)
            val userMoveRef = database.getReference("UserMinutes").child(userUid)
            val userCalRef = database.getReference("UserCalories").child(userUid)
            val userWaterRef = database.getReference("UserWater").child(userUid)
            val userGoalsRef = database.getReference("UserCollectPoints").child(userUid)
            val userSleepHrsRef =database.getReference("UserSleepHours").child(userUid)// UserSleepHours reference for reset

            userLoggedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        var lastLoggedIn = dataSnapshot.getValue(data.LastLoggedIn::class.java)

                        if (lastLoggedIn != null) {
                            if (lastLoggedIn.loggedIndate != null) {
                                val currentDate = Date()
                                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                                val formattedDate = dateFormat.format(currentDate)

                                if(lastLoggedIn.loggedIndate.equals(formattedDate) == false){
                                    //reset all data

                                    //resetting user steps
                                    userStepsRef?.child("_00h00")?.setValue(0)
                                    userStepsRef?.child("_01h00")?.setValue(0)
                                    userStepsRef?.child("_02h00")?.setValue(0)
                                    userStepsRef?.child("_03h00")?.setValue(0)
                                    userStepsRef?.child("_04h00")?.setValue(0)
                                    userStepsRef?.child("_05h00")?.setValue(0)
                                    userStepsRef?.child("_06h00")?.setValue(0)
                                    userStepsRef?.child("_07h00")?.setValue(0)
                                    userStepsRef?.child("_08h00")?.setValue(0)
                                    userStepsRef?.child("_09h00")?.setValue(0)
                                    userStepsRef?.child("_10h00")?.setValue(0)
                                    userStepsRef?.child("_11h00")?.setValue(0)
                                    userStepsRef?.child("_12h00")?.setValue(0)
                                    userStepsRef?.child("_13h00")?.setValue(0)
                                    userStepsRef?.child("_14h00")?.setValue(0)
                                    userStepsRef?.child("_15h00")?.setValue(0)
                                    userStepsRef?.child("_16h00")?.setValue(0)
                                    userStepsRef?.child("_17h00")?.setValue(0)
                                    userStepsRef?.child("_18h00")?.setValue(0)
                                    userStepsRef?.child("_19h00")?.setValue(0)
                                    userStepsRef?.child("_20h00")?.setValue(0)
                                    userStepsRef?.child("_21h00")?.setValue(0)
                                    userStepsRef?.child("_22h00")?.setValue(0)
                                    userStepsRef?.child("_23h00")?.setValue(0)
                                    userStepsRef?.child("_24h00")?.setValue(0)


                                    //resetting move minutes
                                    userMoveRef?.child("minutes")?.setValue(0)

                                    //resetting daily calories
                                    userCalRef?.child("Breakfast")?.child("Calories")?.setValue(0)
                                    userCalRef?.child("Breakfast")?.child("MealName")?.setValue("")

                                    userCalRef?.child("Dinner")?.child("Calories")?.setValue(0)
                                    userCalRef?.child("Dinner")?.child("MealName")?.setValue("")

                                    userCalRef?.child("Lunch")?.child("Calories")?.setValue(0)
                                    userCalRef?.child("Lunch")?.child("MealName")?.setValue("")

                                    userCalRef?.child("Snacks")?.child("Calories")?.setValue(0)
                                    userCalRef?.child("Snacks")?.child("MealName")?.setValue("")


                                    //resetting water
                                    userWaterRef?.child("Amount")?.setValue(0)

                                    //restting collect points
                                    userGoalsRef?.child("stepsGoal")?.setValue(false)
                                    userGoalsRef?.child("moveGoal")?.setValue(false)
                                    userGoalsRef?.child("waterGoal")?.setValue(false)
                                    userGoalsRef?.child("caloriesGoal")?.setValue(false)

                                }
                                userRef?.child("loggedIndate")?.setValue("$formattedDate")
                            }
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
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid
            // Reference to the user's data in the Realtime Database
            val userRef = database.getReference("Users").child(userUid)

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
                            // Assuming myprofileimg is a URL to the image
                            Glide.with(this@MainActivity)
                                .load(myprofileimg)
                                .into(profileimg)

                            Glide.with(this@MainActivity)
                                .load(myprofileimg)
                                .into(profileView)

                            loadAndDisplayBackgroundImage(userDetails.backgroundImageUrl)
                            loadAndDisplayCharacterImage(userDetails.characterImageUrl)


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


        val foodcard = findViewById<CardView>(R.id.foodcardbtn)
        val exercisecard = findViewById<CardView>(R.id.exercisecardbtn)
        val sleepcard = findViewById<CardView>(R.id.sleepcardbtn)
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


        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            userUid = currentUser.uid

            // Now you can use uid in your button click listener
            val bannerItemsButton: Button = findViewById(R.id.button2)
            val characterItemsButton : Button = findViewById(R.id.virtualbtn)

            bannerItemsButton.setOnClickListener {
                val dialogFragment =
                    userUid?.let { it1 -> BannerItemsDialogFragment(it1, this@MainActivity) }
                if (dialogFragment != null) {
                    dialogFragment.show(supportFragmentManager, "BannerItemsDialog")
                }
            }

            characterItemsButton.setOnClickListener {
                val dialogFragment = userUid?.let { it1 -> PFPItemsDialogFragment(it1, this@MainActivity) }
                if (dialogFragment != null) {
                    dialogFragment.show(supportFragmentManager, "PFPItemsDialog")
                }
            }

            val userRef = database.getReference("Users").child(userUid!!)


        }
        //endregion

        //region Button Navigation

        //endregion

    }

    //if back button is pressed then refresh the activity
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
                val intent = Intent(this, WaterSleep::class.java)
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
                        val intent = Intent(this, Welcome::class.java)
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

    //region Banner
    fun onImageClick(imageUrl: String) {
        val imageView: ImageView = findViewById(R.id.imageView)

        // Save the background image URL to the user's profile in the database
        saveBackgroundImageUrl(imageUrl)

        // Load and display the image using Glide or your preferred library
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Make the ImageView visible
        imageView.visibility = View.VISIBLE
    }

    private fun saveBackgroundImageUrl(imageUrl: String) {
        // Save the background image URL to the user's profile in the database
        val userRef = database.getReference("Users").child(userUid!!)
        userRef.child("backgroundImageUrl").setValue(imageUrl)
    }

    private fun loadAndDisplayBackgroundImage(backgroundImageUrl: String) {
        val backgroundImageView: ImageView = findViewById(R.id.imageView)

        // Load and display the background image using Glide or your preferred library
        Glide.with(this)
            .load(backgroundImageUrl)
            .into(backgroundImageView)

        backgroundImageView.visibility = View.VISIBLE
    }

    //endregion

    //region Character
    fun onCharacterImageClick(imageUrl: String) {
        val characterImageView: ImageView = findViewById(R.id.imageView5)

        // Save the character image URL to the user's profile in the database
        saveCharacterImageUrl(imageUrl)

        // Load and display the image using Glide or your preferred library
        Glide.with(this)
            .load(imageUrl)
            .into(characterImageView)

        // Make the ImageView visible
        characterImageView.visibility = View.VISIBLE
    }

    private fun saveCharacterImageUrl(imageUrl: String) {
        // Save the character image URL to the user's profile in the database
        val userRef = database.getReference("Users").child(userUid!!)
        userRef.child("characterImageUrl").setValue(imageUrl)
    }

    private fun loadAndDisplayCharacterImage(characterImageUrl: String) {
        val characterImageView: ImageView = findViewById(R.id.imageView5)

        // Load and display the character image using Glide or your preferred library
        Glide.with(this)
            .load(characterImageUrl)
            .into(characterImageView)

        characterImageView.visibility = View.VISIBLE
    }
    //endregion
}
