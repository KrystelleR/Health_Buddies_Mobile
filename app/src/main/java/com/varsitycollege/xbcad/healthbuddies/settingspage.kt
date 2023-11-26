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
import kotlin.math.roundToInt

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
        val gendertv = findViewById<Spinner>(R.id.gendertxt)
        val emailtv = findViewById<TextView>(R.id.emailtxt)
        val metricsw = findViewById<Switch>(R.id.metricswitch)
        val imperialsw = findViewById<Switch>(R.id.imperialswitch)
        val myweighttv = findViewById<TextView>(R.id.myweighttxt)
        val stepstv = findViewById<Spinner>(R.id.stepsSpinner)
        val minutestv= findViewById<Spinner>(R.id.minutesSpinner)
        val dailywatertv = findViewById<TextView>(R.id.dailywatertxt)
        val caloriestv = findViewById<TextView>(R.id.caloriestxt)
        val sleeptv = findViewById<TextView>(R.id.sleeptxt)
        val aboutmetv = findViewById<EditText>(R.id.aboutmetxt)
        val profiletv = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)

        val spinner: Spinner = findViewById(R.id.minutesSpinner)
        val minValue = 5
        val maxValue = 270
        val increment = 5
        val values = (minValue..maxValue step increment).map { "$it minutes" }
        val minadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
        minadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = minadapter

        imperialsw.setOnCheckedChangeListener { _, isChecked ->
            metricsw.isChecked = !isChecked

            // Change weight and goal weight and height to pounds
            val goalweight = findViewById<TextView>(R.id.weighttxt)
            val currentparts1 = goalweight.text.toString().split(" ")
            val mycurrentweight1 = currentparts1.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val weightinpoundsgoal = if (isChecked) mycurrentweight1 / 0.453592 else mycurrentweight1
            goalweight.text = "${weightinpoundsgoal.roundToInt()} ${if (isChecked) "pounds" else "kg"}"

            val weight = findViewById<TextView>(R.id.myweighttxt)
            val currentparts = weight.text.toString().split(" ")
            val mycurrentweight = currentparts.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val weightinpounds = if (isChecked) mycurrentweight / 0.453592 else mycurrentweight
            weight.text = "${weightinpounds.roundToInt()} ${if (isChecked) "pounds" else "kg"}"

            // Change height to inches
            val height = findViewById<TextView>(R.id.heighttxt)
            val currentparts2 = height.text.toString().split(" ")
            val mycurrentheight = currentparts2.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val heightinches = if (isChecked) mycurrentheight /  2.54 else mycurrentheight
            height.text = "${heightinches.roundToInt()} ${if (isChecked) "inches" else "cm"}"


        }

        metricsw.setOnCheckedChangeListener { _, isChecked ->
            imperialsw.isChecked = !isChecked
            // Change weight and goal weight and height to kg
            val goalweight = findViewById<TextView>(R.id.weighttxt)
            val currentparts1 = goalweight.text.toString().split(" ")
            val mycurrentweight1 = currentparts1.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val weightinkggoal = if (isChecked) mycurrentweight1 * 0.453592 else mycurrentweight1
            goalweight.text = "${weightinkggoal.roundToInt()} ${if (isChecked) "kg" else "pounds"}"

            val weight = findViewById<TextView>(R.id.myweighttxt)
            val currentparts = weight.text.toString().split(" ")
            val mycurrentweight = currentparts.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val weightinkg = if (isChecked) mycurrentweight * 0.453592 else mycurrentweight
            weight.text = "${weightinkg.roundToInt()} ${if (isChecked) "kg" else "pounds"}"

            // Change height to cm
            val height = findViewById<TextView>(R.id.heighttxt)
            val currentparts2 = height.text.toString().split(" ")
            val mycurrentheight = currentparts2.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val heightcm = if (isChecked) mycurrentheight *  2.54 else mycurrentheight
            height.text = "${heightcm.roundToInt()} ${if (isChecked) "cm" else "inches"}"
        }


        heighttv.setOnClickListener(){
            showHeightPicker()
        }
        weighttv.setOnClickListener(){
            showWeightPicker(weighttv.text.toString())
        }

        myweighttv.setOnClickListener(){
            showWeightPickerGoal(myweighttv.text.toString(),weighttv.text.toString(), heighttv.text.toString(), gendertv.selectedItem.toString())
        }


        val stepsSpinner: Spinner = findViewById(R.id.stepsSpinner)
        // Create an array of values representing increments of 100 steps
        var mymin = 1000
        var mymax = 30000
        var myincrement = 1000
        val stepsArray = (mymin..mymax step myincrement).map { "$it steps" }
        // Create an ArrayAdapter using the string array and a default spinner layout
        val stepadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stepsArray)
        // Specify the layout to use when the list of choices appears
        stepadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        stepsSpinner.adapter = stepadapter

// Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.age_array,
            android.R.layout.simple_spinner_item
        )

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
                myage=agetv.selectedItem.toString().toInt()
                caloriestv.text = setDailyCalorieGoal( myage,mygender)
                dailywatertv.text=setDailyWaterGoal(myage,mygender) // update water and calories goal if age change
                sleeptv.text=setUserSleepGoal(myage)
                // This method will be called when an item in the Spinner is selected
                // You can use the 'position' parameter to get the selected item position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if needed
            }
        }

// Set a listener to handle the selected item
        gendertv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mygender=gendertv.selectedItem.toString()
                caloriestv.text = setDailyCalorieGoal( myage,mygender)
                dailywatertv.text=setDailyWaterGoal(myage,mygender)// update water and calories goal if gender change

                // This method will be called when an item in the Spinner is selected
                // You can use the 'position' parameter to get the selected item position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
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

                            // Set the TextView values here
                            heighttv.text = myheight.toString()
                            weighttv.text = myweight.toString()

                            // Set the correct selection based on the value retrieved from the database
                            val genderAdapter = gendertv.adapter as ArrayAdapter<String>
                            val genderPosition = genderAdapter.getPosition(mygender)
                            gendertv.setSelection(genderPosition)


                            // Set the correct selection based on the value retrieved from the database
                            val ageAdapter = agetv.adapter as ArrayAdapter<String>
                            val agePosition = ageAdapter.getPosition(myage.toString())
                            agetv.setSelection(agePosition)


                            // Set the correct selection based on the value retrieved from the database
                            val stepAdapter = stepsSpinner.adapter as ArrayAdapter<String>
                            val stepPosition = stepAdapter.getPosition("$mydailysteps steps" )
                            stepsSpinner.setSelection(stepPosition)

                            // Set the correct selection based on the value retrieved from the database
                            val minAdapter = minutestv.adapter as ArrayAdapter<String>
                            val minPosition = minAdapter.getPosition("$mymoveminutes minutes")
                            minutestv.setSelection(minPosition)


                            emailtv.text = myemail
                            usernametv.setText(myusername)
                            currentProfileImageResourceId = myprofileimg
                            profiletv.setImageResource(myprofileimg)
                            myweighttv.text = mygoalweight.toString()
                            dailywatertv.text = mywatergoal.toString()
                            dailywatertv.text = setDailyWaterGoal(myage,mygender)//set water goal based on age and sex
                            caloriestv.text = mydailycalories.toString()
                            caloriestv.text = setDailyCalorieGoal( myage,mygender)// set calories goal based on age and sex
                            Toast.makeText(this@settingspage, "$myage and $mygender", Toast.LENGTH_SHORT).show()


                            sleeptv.text = mysleepgoal.toString()
                            sleeptv.text=setUserSleepGoal(myage)

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

                            // Update the TextView values here
                            heighttv.text = myheight
                            weighttv.text = myweight

// Set the correct selection based on the value retrieved from the database
                            val weightParts = myweight.split(" ")
                            val mycurrentweight = weightParts.firstOrNull()?.toDoubleOrNull() ?: 0.0

                            if (myimperial) {
                                // Convert weight to pounds if the imperial switch is on
                                val weightinpounds = mycurrentweight * 2.20462
                                weighttv.text = "${weightinpounds.roundToInt()} pounds"
                            } else {
                                // Display weight in kg if the metric switch is on
                                weighttv.text = "${mycurrentweight.roundToInt()} kg"
                            }

// Set the correct selection based on the value retrieved from the database
                            val goalWeightParts = mygoalweight.split(" ")
                            val mycurrentGoalWeight = goalWeightParts.firstOrNull()?.toDoubleOrNull() ?: 0.0

                            if (myimperial) {
                                // Convert goal weight to pounds if the imperial switch is on
                                val goalWeightInPounds = mycurrentGoalWeight * 2.20462
                                myweighttv.text = "${goalWeightInPounds.roundToInt()} pounds"
                            } else {
                                // Display goal weight in kg if the metric switch is on
                                myweighttv.text = "${mycurrentGoalWeight.roundToInt()} kg"
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


        val save = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.savebtn)
        save.setOnClickListener(){
            saveChangesToDatabase()
        }

    }


    fun extractWeightValue(weightString: String): Int {
        // Define a regex pattern to match the numeric part of the string
        val regex = Regex("""(\d+)""")

        // Find the match in the input string
        val matchResult = regex.find(weightString)

        // Extract the matched group (numeric part) and convert it to an Int
        return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }


    private fun saveChangesToDatabase() {
        val usernametv = findViewById<EditText>(R.id.usernametxt)
        val agetv = findViewById<Spinner>(R.id.ageSpinner)
        val heighttv = findViewById<TextView>(R.id.heighttxt)
        val weighttv = findViewById<TextView>(R.id.weighttxt)
        val gendertv = findViewById<Spinner>(R.id.gendertxt)
        val metricsw = findViewById<Switch>(R.id.metricswitch)
        val imperialsw = findViewById<Switch>(R.id.imperialswitch)
        val stepstv = findViewById<Spinner>(R.id.stepsSpinner)
        val myweighttv = findViewById<TextView>(R.id.myweighttxt)
        val dailywatertv = findViewById<TextView>(R.id.dailywatertxt)
        val caloriestv = findViewById<TextView>(R.id.caloriestxt)
        val sleeptv = findViewById<TextView>(R.id.sleeptxt)
        val aboutmetv = findViewById<EditText>(R.id.aboutmetxt)
        val spinner: Spinner = findViewById(R.id.minutesSpinner)

        val selectedMinutesString = spinner.selectedItem.toString()
        val parts = selectedMinutesString.split(" ")
        val selectedMinutesInt = parts.firstOrNull()?.toIntOrNull() ?: 0



        val selectedStepsString = stepstv.selectedItem.toString()
        val theparts = selectedStepsString.split(" ")
        val selectedStepsInt = theparts.firstOrNull()?.toIntOrNull() ?: 0

        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val userUid = currentUser.uid
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userUid)


            // Create an instance of UserDetails with the updated values
            val updatedUserDetails = currentUser.email?.let {
                data.UserDetails(
                    currentUser.uid,
                    usernametv.text.toString(),
                    it,
                    agetv.selectedItem.toString().toInt(),
                    heighttv.text.toString(),
                    myweighttv.text.toString(),
                    metricsw.isChecked,
                    imperialsw.isChecked,
                    currentProfileImageResourceId,
                    true,  // Assuming this should always be set to true when saving changes
                    gendertv.selectedItem.toString(),
                    aboutmetv.text.toString(),
                    myusercurrency,
                    mycurrentcalories,
                    selectedStepsInt,  // Assuming steps can be converted to Int
                    weighttv.text.toString(),
                    selectedMinutesInt,
                    sleeptv.text.toString().toInt(), // Assuming minutes can be converted to Int
                    dailywatertv.text.toString().toInt(),  // Assuming water goal can be converted to Int // Assuming sleep goal can be converted to Int
                    caloriestv.text.toString().toInt()
                )
            }

            // Update the values in the database using the UserDetails instance
            userRef.setValue(updatedUserDetails)

            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show()
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

    fun setDailyCalorieGoal(age:Int, sex: String) : String{

        var userCalorieGoal : String =" "
        if(age>=4 && age<=8 && sex.equals("M")){

            userCalorieGoal="1500"

        }else if(age>=4 && age<=8 && sex.equals("F")){

            userCalorieGoal="1500"
        }

        else if(age>=9 && age<=13 && sex.equals("M")){

            userCalorieGoal="2000"

        }else if(age>=9 && age<=13 && sex.equals("F")){

            userCalorieGoal="1800"

        }
        else if(age>=14 && age<=18 && sex.equals("M")){

            userCalorieGoal="2600"

        }else if(age>=14 && age<=18 && sex.equals("F")){

            userCalorieGoal="2000"

        }

        return userCalorieGoal
    }

    //set water goal
    fun setDailyWaterGoal(age:Int, sex: String) : String{

        var userWaterGoal : String =" "
        if(age>=4 && age<=8 && sex.equals("M")){

            userWaterGoal="1200"

        }else if(age>=4 && age<=8 && sex.equals("F")){

            userWaterGoal="1200"
        }

        else if(age>=9 && age<=13 && sex.equals("M")){

            userWaterGoal="1600"

        }else if(age>=9 && age<=13 && sex.equals("F")){

            userWaterGoal="1400"

        }
        else if(age>=14 && age<=18 && sex.equals("M")){

            userWaterGoal="1900"

        }else if(age>=14 && age<=18 && sex.equals("F")){

            userWaterGoal="1600"

        }

        return userWaterGoal
    }

    fun setUserSleepGoal (age: Int): String{

        var sleepGoal :String = " "
        if(age>=5 && age<=12){

            sleepGoal="11"
        }else if(age>=13 && age<=18){
            sleepGoal="9"
        }
        return sleepGoal
    }

    fun showWeightPicker(chosenWeight:String) {
        // Set up the dialog and handle the OK button click
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.number_picker_dialog)

        val metrricsw = findViewById<Switch>(R.id.metricswitch)


        val parts = chosenWeight.split(" ")
        val thecurrentweight = parts.firstOrNull()?.toIntOrNull() ?: 0

        // Initialize the NumberPicker with the desired range
        val weightPicker = dialog.findViewById<NumberPicker>(R.id.dialogNumberPicker)

        if(metrricsw.isChecked){
            weightPicker.minValue = 10
            weightPicker.maxValue = 200
            weightPicker.value = thecurrentweight

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
            weightPicker.value = thecurrentweight

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

    fun showWeightPickerGoal(chosenWeight:String, currentWeight:String, currentHeight:String, gender:String) {
        // Set up the dialog and handle the OK button click
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.number_picker_dialog_goal)

        val metrricsw = findViewById<Switch>(R.id.metricswitch)


        val parts = chosenWeight.split(" ")
        val thecurrentweight = parts.firstOrNull()?.toIntOrNull() ?: 0

        // Initialize the NumberPicker with the desired range
        val weightPicker = dialog.findViewById<NumberPicker>(R.id.dialogNumberPicker)

        if(metrricsw.isChecked){
            weightPicker.minValue = 10
            weightPicker.maxValue = 200


            val unit = dialog.findViewById<TextView>(R.id.unittxt)
            unit.text= " kg"

            val currentparts = currentWeight.split(" ")
            val mycurrentweight = currentparts.firstOrNull()?.toIntOrNull() ?: 0

            val currentpartsheight = currentHeight.split(" ")
            var mycurrentheight = currentpartsheight.firstOrNull()?.toDoubleOrNull() ?: 0.0

            mycurrentheight = (mycurrentheight.toDouble() / 100.0)  // Convert height to meters
            val calbmi = mycurrentweight / (mycurrentheight * mycurrentheight)

            val bmiInInt = calbmi.roundToInt()
            var state = ""
            if(calbmi <=18.5){
               state = "underweight"
            }
            else if(calbmi >= 25.0){
                state = "overweight"
            }
            else{
                state = "healthy weight"
            }

            var recommendedGoalWeight = 0.0
            if(gender == "M"){
                recommendedGoalWeight = 22 * mycurrentheight * 2
            }
            else{
                recommendedGoalWeight = 22 * (mycurrentheight - 10) * 2
            }

            val recommendedGoalWeightInt = recommendedGoalWeight.roundToInt()
            val bmigoal = dialog.findViewById<TextView>(R.id.bmitxt)
            bmigoal.text = "Your current BMI is: $bmiInInt ($state) \nThe best weight for you would be:$recommendedGoalWeightInt kg\n\nBut pick a goal that is realistic for you!\""

            weightPicker.value = recommendedGoalWeightInt
            val okButton: Button = dialog.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Update the TextView with the selected height
                val weightTextView = findViewById<TextView>(R.id.myweighttxt)
                weightTextView.text = "${weightPicker.value} kg"
                dialog.dismiss()
            }
        }
        else{
            weightPicker.minValue = 30
            weightPicker.maxValue = 440

            val unit = dialog.findViewById<TextView>(R.id.unittxt)
            unit.text= " pounds"


            val currentparts = currentWeight.split(" ")
            var mycurrentweight = currentparts.firstOrNull()?.toDoubleOrNull() ?: 0.0

            val currentpartsheight = currentHeight.split(" ")
            var mycurrentheight = currentpartsheight.firstOrNull()?.toDoubleOrNull() ?: 0.0

            //from pounds to kg
            mycurrentweight = mycurrentweight * 0.453592

            //inches to cm
            mycurrentheight = mycurrentheight * 2.54




            mycurrentheight = (mycurrentheight.toDouble() / 100.0)  // Convert height to meters
            val calbmi = mycurrentweight / (mycurrentheight * mycurrentheight)

            val bmiInInt = calbmi.roundToInt()
            var state = ""
            if(calbmi <=18.5){
                state = "underweight"
            }
            else if(calbmi >= 25.0){
                state = "overweight"
            }
            else{
                state = "healthy weight"
            }

            var recommendedGoalWeight = 0.0
            if(gender == "M"){
                recommendedGoalWeight = 22 * mycurrentheight * 2
            }
            else{
                recommendedGoalWeight = 22 * (mycurrentheight - 10) * 2
            }

            recommendedGoalWeight = recommendedGoalWeight * 2.20462
            val recommendedGoalWeightInt = recommendedGoalWeight.roundToInt()



            val bmigoal = dialog.findViewById<TextView>(R.id.bmitxt)
            bmigoal.text = "Your current BMI is: $bmiInInt ($state) \nThe best weight for you would be:$recommendedGoalWeightInt pounds\n\nBut pick a goal that is realistic for you!\""
            weightPicker.value = recommendedGoalWeightInt





            val okButton: Button = dialog.findViewById(R.id.okButton)
            okButton.setOnClickListener {
                // Update the TextView with the selected height
                val weightTextView = findViewById<TextView>(R.id.myweighttxt)
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


    private var currentProfileImageResourceId: Int =0

    private fun setProfileImage(resourceId: Int) {
        val profileImageView = findViewById<ImageView>(R.id.profile_image)
        profileImageView.setImageResource(resourceId)
        currentProfileImageResourceId = resourceId
    }
}