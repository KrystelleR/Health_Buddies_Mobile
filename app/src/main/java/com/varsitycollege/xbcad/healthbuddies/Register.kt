package com.varsitycollege.xbcad.healthbuddies

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        val database = FirebaseDatabase.getInstance()
        auth = Firebase.auth

        val btnRegister = findViewById<Button>(R.id.registerbtn)
        val emailEditText = findViewById<EditText>(R.id.emailtxt)
        val passwordEditText = findViewById<EditText>(R.id.passwordtxt)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confpasswordtxt)


        var isEmail = false
        var isPassword = false
        var isConfirmPassword = false

        val loginbtn = findViewById<Button>(R.id.Signinbtn)
        loginbtn.setOnClickListener(){
            val Intent = Intent(this, Login::class.java)
            startActivity(Intent)
        }

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.error =("Please enter a valid email address")
                    isEmail = false
                }  else {
                    emailEditText.error = null // Clear the error
                    isEmail = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                val passwordRegex = "(?=.*[A-Z])(?=.*[0-9]).{6,}".toRegex()

                if (!passwordRegex.matches(password)) {
                    passwordEditText.error = "Password must be at least 6 characters long, contain a capital letter, and a numerical value"
                    isPassword = false
                } else {
                    passwordEditText.error = null // Clear the error
                    isPassword = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        confirmPasswordEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val confirmPassword = s.toString()
                val password = passwordEditText.text.toString() // Retrieve the password from passwordEditText

                if (confirmPassword != password) { // Compare the passwords
                    confirmPasswordEditText.error = "Passwords do not match" // Set error on confirmPassText
                    isConfirmPassword = false
                } else {
                    confirmPasswordEditText.error = null // Clear the error
                    isConfirmPassword = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        btnRegister.setOnClickListener {
            val usernameEditText = findViewById<EditText>(R.id.usernametxt)
            val usernameText = usernameEditText.text.toString()

            val emailEditText = findViewById<EditText>(R.id.emailtxt)
            val emailText = emailEditText.text.toString()

            val passswordEditText = findViewById<EditText>(R.id.passwordtxt)
            val passswordText = passswordEditText.text.toString()

            val passswordConfirmEditText = findViewById<EditText>(R.id.confpasswordtxt)
            val passswordConfirmText = passswordConfirmEditText.text.toString()

            // Check if all fields are not empty and formatted correctly
            if (usernameText.isNotEmpty() && emailText.isNotEmpty() && passswordText.isNotEmpty() && passswordConfirmText.isNotEmpty() && isEmail && isConfirmPassword && isPassword) {
                // Check if the email is already registered
                auth.fetchSignInMethodsForEmail(emailText)
                    .addOnCompleteListener { fetchTask ->
                        if (fetchTask.isSuccessful) {
                            val signInMethods = fetchTask.result?.signInMethods
                            if (signInMethods != null && signInMethods.isNotEmpty()) {
                                // Email is already registered
                                Toast.makeText(
                                    baseContext,
                                    "Email already registered. Go to Login Page.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Email is not registered, proceed with registration
                                auth.createUserWithEmailAndPassword(emailText, passswordText)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            // Sign in success, update user's profile with the name
                                            val user = auth.currentUser
                                            val usersRef = database.getReference("Users")
                                            // Set user details in your data class
                                            val userDetails = data.UserDetails(
                                                uid  = user?.uid ?: "",
                                                username  = usernameText,
                                                email = emailText,
                                                age=5,
                                                height="100 cm",
                                                weight="10 Kg",
                                                metric=  true,
                                                imperial=false,
                                                profileImage="https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/profile_images%2F1.png?alt=media&token=ec07670f-fd2f-4c75-b9b3-34f8bd701c2d",
                                                setDetails = false,
                                                gender ="M",
                                                aboutMe = "",
                                                userCurrency =0,
                                                userCurrentCalories =0,
                                                dailySteps =1000,
                                                goalWeight  ="15 Kg",
                                                moveMinutes =30,
                                                sleep  =8,
                                                dailyWaterAmount  =1500,
                                                dailyCalories =2000,
                                                backgroundImageUrl ="https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2FFrame%201.png?alt=media&token=b8266396-ccc4-430a-b7b2-57ddc4128d8a",
                                                characterImageUrl = "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/characters_images%2FChar3.png?alt=media&token=60d27a9c-f255-4800-b8dc-5f85e4f4e6b3"
                                            )

                                            Log.d(TAG, "Character Image URL: ${userDetails.characterImageUrl}")

                                            val purchasedItemsRef = database.getReference("PurchasedItems")
                                            val userBannerRef = purchasedItemsRef.child(userDetails.uid).child("banner")
                                            val userCharacterRef = purchasedItemsRef.child(userDetails.uid).child("characters")
                                            val userPFPRef = purchasedItemsRef.child(userDetails.uid).child("pfp")

                                            val purchasedItem = data.PurchasedItem(
                                                ID = "-NjrybjVjL6vsqsP5Rmi",
                                                ImageUrl = "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/images%2FFrame%201.png?alt=media&token=b8266396-ccc4-430a-b7b2-57ddc4128d8a",
                                                setValueTrue = true
                                            )

                                            userBannerRef.child("NjrybjVjL6vsqsP5Rmi").child("ID").setValue(purchasedItem.ID)
                                            userBannerRef.child("NjrybjVjL6vsqsP5Rmi").child("ImageUrl").setValue(purchasedItem.ImageUrl)
                                            userBannerRef.child("NjrybjVjL6vsqsP5Rmi").child("setValueTrue").setValue(purchasedItem.setValueTrue)


                                            val characterPurchasedItem = data.PurchasedItem(
                                                ID = "-NjrybjVjL6vsqsP5Rej",
                                                ImageUrl = "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/characters_images%2FChar3.png?alt=media&token=60d27a9c-f255-4800-b8dc-5f85e4f4e6b3",
                                                setValueTrue = true
                                            )

                                            userCharacterRef.child("NjrybjVjL6vsqsP5Rmi").child("ID").setValue(characterPurchasedItem.ID)
                                            userCharacterRef.child("NjrybjVjL6vsqsP5Rmi").child("ImageUrl").setValue(characterPurchasedItem.ImageUrl)
                                            userCharacterRef.child("NjrybjVjL6vsqsP5Rmi").child("setValueTrue").setValue(characterPurchasedItem.setValueTrue)



                                            val pfpRef = data.PurchasedItem(
                                                ID = "-NjrgtdVjL6vsqsP5Rej",
                                                ImageUrl = "https://firebasestorage.googleapis.com/v0/b/healthbuddies-48435.appspot.com/o/profile_images%2F1.png?alt=media&token=ec07670f-fd2f-4c75-b9b3-34f8bd701c2d",
                                                setValueTrue = true
                                            )
                                            userPFPRef.child("NjrybjVjL6vsqsP5Rmi").child("ID").setValue(pfpRef.ID)
                                            userPFPRef.child("NjrybjVjL6vsqsP5Rmi").child("ImageUrl").setValue(pfpRef.ImageUrl)
                                            userPFPRef.child("NjrybjVjL6vsqsP5Rmi").child("setValueTrue").setValue(pfpRef.setValueTrue)


                                            val lastLoggedIn = Date()
                                            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                                            val formattedDate = dateFormat.format(lastLoggedIn)

                                            val usersRefLoggedIn = database.getReference("LastLoggedIn")

                                                val loggedIn = data.LastLoggedIn(
                                                    loggedIndate = formattedDate
                                                )


                                            val userGoalRef = database.getReference("UserCollectPoints")
                                            val collectPoints = data.UserCollectPoints(
                                                stepsGoal = false,
                                                moveGoal = false,
                                                waterGoal = false,
                                                caloriesGoal = false
                                            )



                                            val usersRef1 = database.getReference("UserSteps")
                                            // Set user details in your data class
                                            val userSteps = data.UserSteps(
                                                _00h00 =0,
                                                _01h00 =0,
                                                _02h00 =0,
                                                _03h00 =0,
                                                _04h00 =0,
                                                _05h00 =0,
                                                _06h00 =0,
                                                _07h00 =0,
                                                _08h00 =0,
                                                _09h00 =0,
                                                _10h00 =0,
                                                _11h00 =0,
                                                _12h00 =0,
                                                _13h00 =0,
                                                _14h00 =0,
                                                _15h00 =0,
                                                _16h00 =0,
                                                _17h00 =0,
                                                _18h00 =0,
                                                _19h00 =0,
                                                _20h00 =0,
                                                _21h00 =0,
                                                _22h00 =0,
                                                _23h00 =0
                                            )

                                            val usersRef2 = database.getReference("UserMinutes")
                                            // Set user details in your data class
                                            val UserMoveMinutes = data.UserMinutes(
                                                minutes=0
                                            )

                                            val profileUpdates = UserProfileChangeRequest.Builder()
                                                .setDisplayName(usernameText) // Set the display name
                                                .build()

                                            user?.updateProfile(profileUpdates)
                                                ?.addOnCompleteListener { profileUpdateTask ->
                                                    if (profileUpdateTask.isSuccessful) {
                                                        Log.d(ContentValues.TAG, "User profile updated.")
                                                        auth.currentUser?.sendEmailVerification()
                                                            ?.addOnSuccessListener {

                                                                // Add the user details to Realtime Database
                                                                usersRef.child(userDetails.uid).setValue(userDetails)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "User details added to Realtime Database successfully")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error adding user details to Realtime Database", e)
                                                                    }

                                                                // for UserCalories nodes
                                                                initializeUserNode()
                                                                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()

                                                                // Add the user steps to Realtime Database
                                                                usersRef1.child(userDetails.uid).setValue(userSteps)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "User Steps added to Realtime Database successfully")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error adding user steps to Realtime Database", e)
                                                                    }

                                                                usersRefLoggedIn.child(userDetails.uid).setValue(loggedIn)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "User loggedIn date added to Realtime Database successfully")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error adding user loggedIn date to Realtime Database", e)
                                                                    }

                                                                userGoalRef.child(userDetails.uid).setValue(collectPoints)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "User Collect Points added to Realtime Database successfully")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error adding user User Collect Points date to Realtime Database", e)
                                                                    }


                                                                // Add the user move minutes to Realtime Database
                                                                usersRef2.child(userDetails.uid).setValue(UserMoveMinutes)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "User Move Minutes added to Realtime Database successfully")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error adding user Move Minutes to Realtime Database", e)
                                                                    }
                                                            }
                                                            ?.addOnFailureListener{
                                                                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                                                            }
                                                        //updateUI()
                                                    } else {
                                                        Log.w(ContentValues.TAG, "Failed to update user profile.")
                                                        // Handle the error here
                                                    }
                                                }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                                            // Authentication failed because of some other reason
                                            Toast.makeText(
                                                baseContext,
                                                "Authentication failed.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }


                                    }
                            }
                        } else {
                            // Error occurred while checking if email is registered
                            Log.w(ContentValues.TAG, "fetchSignInMethodsForEmail:failure", fetchTask.exception)
                            Toast.makeText(
                                baseContext,
                                "Error checking email availability.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                // If fields are incorrectly formatted or empty, show an error message
                Toast.makeText(
                    baseContext,
                    "Please fill in all fields correctly.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    //create breakfast, lunch , dinner usercalories nodes on db if successful sign up
    private fun initializeUserNode() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Initialize user node with empty sub-nodes for Breakfast, Lunch, and Dinner
            val userCaloriesRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)
            // Create a reference to the "UserWater" node
            val userWaterRef = FirebaseDatabase.getInstance().getReference("UserWater").child(userId)

            //create a reference to the UserSleepHours node
            val userSleepRef =FirebaseDatabase.getInstance().getReference("UserSleepHours").child(userId)
            userSleepRef.child("Sun-Mon").child("Hours").setValue(0)
            userSleepRef.child("Mon-Tue").child("Hours").setValue(0)
            userSleepRef.child("Tue-Wed").child("Hours").setValue(0)
            userSleepRef.child("Wed-Thur").child("Hours").setValue(0)
            userSleepRef.child("Thur-Fri").child("Hours").setValue(0)
            userSleepRef.child("Fri-Sat").child("Hours").setValue(0)
            userSleepRef.child("Sat-Sun").child("Hours").setValue(0)


            //Initialize water node
            userWaterRef.child("Amount").setValue(0)

            // Initialize Breakfast node
            userCaloriesRef.child("Breakfast").child("MealName").setValue(" ")
            userCaloriesRef.child("Breakfast").child("Calories").setValue(0)

            // Initialize Lunch node
            userCaloriesRef.child("Lunch").child("MealName").setValue(" ")
            userCaloriesRef.child("Lunch").child("Calories").setValue(0)

            // Initialize Dinner node
            userCaloriesRef.child("Dinner").child("MealName").setValue(" ")
            userCaloriesRef.child("Dinner").child("Calories").setValue(0)

            // Initialize Snacks node
            userCaloriesRef.child("Snacks").child("MealName").setValue(" ")
            userCaloriesRef.child("Snacks").child("Calories").setValue(0)
        }
    }

    //to-do

    //see why authentication failed (email taken)

    private fun updateUI() {
        val Intent = Intent(this, settingspage::class.java)
        startActivity(Intent)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI()
        }
    }
}