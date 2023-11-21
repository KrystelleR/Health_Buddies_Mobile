package com.varsitycollege.xbcad.healthbuddies

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class settingspage : AppCompatActivity() {

    var myuid: String=""
    var mysetDetails: Boolean =false
    var myusername: String? =""
    var myemail: String =""
    var myage: Int =0
    var myheight: String = ""
    var myweight: String = ""
    var mymetric: Boolean=  true
    var myimperial:Boolean= false
    var myprofileimg: Int =0
    var mydailysteps: Int=0
    var mymoveminutes: Int=0
    var mygoalweight: String = ""
    var mywatergoal: Int =0
    var mysleepgoal: Int =0
    var mydailycalories: Int =0
    var mygender: String = ""
    var myaboutme: String = ""
    var myusercurrency: Int =0
    var mycurrentcalories: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingspage)

        //get all values
        val usernametv = findViewById<EditText>(R.id.usernametxt)
        val agetv = findViewById<Spinner>(R.id.ageSpinner)
        val heighttv = findViewById<TextView>(R.id.heighttxt)
        val weighttv = findViewById<TextView>(R.id.weighttxt)
        val gendertv = findViewById<TextView>(R.id.gendertxt)
        val emailtv = findViewById<TextView>(R.id.emailtxt)
        val metricsw = findViewById<Switch>(R.id.metricswitch)
        val imperialsw = findViewById<Switch>(R.id.imperialswitch)
        val stepstv = findViewById<TextView>(R.id.stepstxt)
        val myweighttv = findViewById<TextView>(R.id.myweighttxt)
        val minutestv = findViewById<TextView>(R.id.minutestxt)
        val dailywatertv = findViewById<TextView>(R.id.dailywatertxt)
        val caloriestv = findViewById<TextView>(R.id.caloriestxt)
        val sleeptv = findViewById<TextView>(R.id.sleeptxt)
        val aboutmetv = findViewById<EditText>(R.id.aboutmetxt)
        val profiletv = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)

        imperialsw.isChecked = !metricsw.isChecked
        metricsw.isChecked = !imperialsw.isChecked

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.age_array,
            android.R.layout.simple_spinner_item
        )

        heighttv.setOnClickListener(){
            showHeightPicker()
        }

        weighttv.setOnClickListener(){
            showWeightPicker()
        }


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        agetv.adapter = adapter

        // Set a listener to handle the selected item
        agetv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedAge = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if needed
            }
        }


        val pickImage = findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.add_image)
        pickImage.setOnClickListener() {
            pickImage()
        }




        val database = FirebaseDatabase.getInstance()

// Getting user details from db
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid
            // Reference to the user's data in the Realtime Database
            val userRef = database.getReference("Users").child(userUid) // Corrected line

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val userDetails = dataSnapshot.getValue(data.UserDetails::class.java)

                        // Now you can use the userDetails object as needed
                        if (userDetails != null) {
                            myuid = currentUser.uid
                            myusername = userDetails.username
                            myemail = userDetails.email
                            myage = userDetails.age
                            myheight = userDetails.height
                            myweight = userDetails.weight
                            mymetric = userDetails.metric
                            myimperial = userDetails.imperial
                            myprofileimg = userDetails.profileImage
                            mysetDetails = userDetails.setDetails
                            mygender = userDetails.gender
                            myaboutme = userDetails.aboutMe
                            myusercurrency = userDetails.userCurrency
                            mydailysteps = userDetails.dailySteps
                            mygoalweight = userDetails.goalWeight
                            mymoveminutes = userDetails.moveMinutes
                            mywatergoal = userDetails.dailyWaterAmount
                            mysleepgoal = userDetails.sleep
                            mydailycalories = userDetails.dailyCalories
                            mycurrentcalories = userDetails.userCurrentCalories

                            Toast.makeText(this@settingspage, "my height: ${myheight}", Toast.LENGTH_SHORT).show()

                            // Set the TextView values here
                            heighttv.text = myheight.toString()
                            weighttv.text = myweight.toString()
                            gendertv.text = mygender
                            emailtv.text = myemail
                            usernametv.setText(myusername)
                            profiletv.setImageResource(myprofileimg)
                            stepstv.text = mydailysteps.toString()
                            myweighttv.text = mygoalweight.toString()
                            minutestv.text = mymoveminutes.toString()
                            dailywatertv.text = mywatergoal.toString()
                            caloriestv.text = mydailycalories.toString()
                            sleeptv.text = mysleepgoal.toString()

                            if(mymetric){
                                metricsw.isChecked = true
                                imperialsw.isChecked = false
                            }
                            else{
                                metricsw.isChecked = false
                                imperialsw.isChecked = true
                            }

                            if(myaboutme!=""){
                                aboutmetv.setText(myaboutme)
                            }


                        }
                    } else {
                        Log.d(TAG, "User details do not exist")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error reading user details from the database", databaseError.toException())
                }
            })
        }
    }

    private fun pickImage() {
        val edit = Dialog(this)
        edit.setContentView(R.layout.pickimagedialog)

        val img1 = edit.findViewById<CardView>(R.id.img1cv)
        val img2 = edit.findViewById<CardView>(R.id.img2cv)
        val img3 = edit.findViewById<CardView>(R.id.img3cv)
        val img4 = edit.findViewById<CardView>(R.id.img4cv)
        val img5 = edit.findViewById<CardView>(R.id.img5cv)
        val img6 = edit.findViewById<CardView>(R.id.img6cv)
        val img7 = edit.findViewById<CardView>(R.id.img7cv)
        val img8 = edit.findViewById<CardView>(R.id.img8cv)
        val img9 = edit.findViewById<CardView>(R.id.img9cv)
        val img10 = edit.findViewById<CardView>(R.id.img10cv)
        val img11 = edit.findViewById<CardView>(R.id.img11cv)
        val img12 = edit.findViewById<CardView>(R.id.img12cv)

        img1.setOnClickListener(){
            setProfileImage(R.drawable.profileimg1)
            edit.dismiss()
        }
        img2.setOnClickListener(){
            setProfileImage(R.drawable.profileimg2)
            edit.dismiss()
        }
        img3.setOnClickListener(){
            setProfileImage(R.drawable.profileimg3)
            edit.dismiss()
        }
        img4.setOnClickListener(){
            setProfileImage(R.drawable.profileimg4)
            edit.dismiss()
        }
        img5.setOnClickListener(){
            setProfileImage(R.drawable.profileimg5)
            edit.dismiss()
        }
        img6.setOnClickListener(){
            setProfileImage(R.drawable.profileimg6)
            edit.dismiss()
        }
        img7.setOnClickListener(){
            setProfileImage(R.drawable.profileimg7)
            edit.dismiss()
        }
        img8.setOnClickListener(){
            setProfileImage(R.drawable.profileimg8)
            edit.dismiss()
        }
        img9.setOnClickListener(){
            setProfileImage(R.drawable.profileimg9)
            edit.dismiss()
        }
        img10.setOnClickListener(){
            setProfileImage(R.drawable.profileimg10)
            edit.dismiss()
        }
        img11.setOnClickListener(){
            setProfileImage(R.drawable.profileimg11)
            edit.dismiss()
        }
        img12.setOnClickListener(){
            setProfileImage(R.drawable.profileimg12)
            edit.dismiss()
        }

        edit.show()
    }


    fun showWeightPicker() {
        // Set up the dialog and handle the OK button click
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.number_picker_dialog)

        val metrricsw = findViewById<Switch>(R.id.metricswitch)

        // Initialize the NumberPicker with the desired range
        val weightPicker = dialog.findViewById<NumberPicker>(R.id.dialogNumberPicker)

        if(metrricsw.isChecked){
            weightPicker.minValue = 10
            weightPicker.maxValue = 200
            weightPicker.value = 30

            val unit = dialog.findViewById<TextView>(R.id.unittxt)
            unit.text= " kg"

            val okButton: Button = dialog.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Update the TextView with the selected height
                val weightTextView = findViewById<TextView>(R.id.weighttxt)
                weightTextView.text = "${weightPicker.value} kg"
                dialog.dismiss()
            }
        }
        else{
            weightPicker.minValue = 30
            weightPicker.maxValue = 440
            weightPicker.value = 100

            val unit = dialog.findViewById<TextView>(R.id.unittxt)
            unit.text= " pounds"

            val okButton: Button = dialog.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Update the TextView with the selected height
                val weightTextView = findViewById<TextView>(R.id.weighttxt)
                weightTextView.text = "${weightPicker.value} pounds"
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showHeightPicker() {
        // Set up the dialog and handle the OK button click
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.number_picker_dialog)

        val metrricsw = findViewById<Switch>(R.id.metricswitch)


        // Initialize the NumberPicker with the desired range
        val heightPicker = dialog.findViewById<NumberPicker>(R.id.dialogNumberPicker)

        if(metrricsw.isChecked){
            heightPicker.minValue = 100
            heightPicker.maxValue = 300
            heightPicker.value = 130

            val unit = dialog.findViewById<TextView>(R.id.unittxt)
            unit.text= " cm"

            val okButton: Button = dialog.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Update the TextView with the selected height
                val heightTextView = findViewById<TextView>(R.id.heighttxt)
                heightTextView.text = "${heightPicker.value} cm"
                dialog.dismiss()
            }
        }
        else{
            heightPicker.minValue = 40
            heightPicker.maxValue = 120
            heightPicker.value = 50

            val unit = dialog.findViewById<TextView>(R.id.unittxt)
            unit.text= " inches"

            val okButton: Button = dialog.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Update the TextView with the selected height
                val heightTextView = findViewById<TextView>(R.id.heighttxt)
                heightTextView.text = "${heightPicker.value} inches"
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun setProfileImage(resourceId: Int) {
        val profileImageView = findViewById<ImageView>(R.id.profile_image)
        profileImageView.setImageResource(resourceId)
    }
}