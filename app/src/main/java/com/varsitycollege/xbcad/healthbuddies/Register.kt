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
                                                profileImage=R.drawable.profileimg1,
                                                setDetails = false,
                                                gender ="M",
                                                aboutMe = ""
                                            )

                                            // Set user goals in your data class
                                            val userGoals = data.UserGoals(
                                                dailySteps =1000,
                                                goalWeight  ="15 Kg",
                                                moveMinutes =30,
                                                sleep  =8,
                                                dailyWaterAmount  =1500,
                                                dailyCalories =2000,
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

                                                                // Add the user goals to Realtime Database
                                                                // Assuming you have a "user_goals" child under the user's UID
                                                                usersRef.child(userDetails.uid).child("user_goals").setValue(userGoals)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "User goals added to Realtime Database successfully")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error adding user goals to Realtime Database", e)
                                                                    }
                                                                // for UserCalories nodes
                                                                initializeUserNode()
                                                                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
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

    //to-do

    //see why authentication failed (email taken)

    private fun updateUI() {
        val Intent = Intent(this, MainActivity::class.java)
        startActivity(Intent)
    }

    //create breakfast, lunch , dinner usercalories nodes on db if successful sign up
    private fun initializeUserNode() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            // Initialize user node with empty sub-nodes for Breakfast, Lunch, and Dinner
            val userCaloriesRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)

            // Initialize Breakfast node
            userCaloriesRef.child("Breakfast").child("MealName").setValue(" ")
            userCaloriesRef.child("Breakfast").child("Calories").setValue(0)

            // Initialize Lunch node
            userCaloriesRef.child("Lunch").child("MealName").setValue(" ")
            userCaloriesRef.child("Lunch").child("Calories").setValue(0)

            // Initialize Dinner node
            userCaloriesRef.child("Dinner").child("MealName").setValue(" ")
            userCaloriesRef.child("Dinner").child("Calories").setValue(0)
        }
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