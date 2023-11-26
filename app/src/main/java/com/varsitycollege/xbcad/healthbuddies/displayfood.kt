package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import com.varsitycollege.xbcad.healthbuddies.databinding.ActivityDisplayfoodBinding
import org.w3c.dom.Text
import kotlin.math.roundToInt

class displayfood : AppCompatActivity() {

    private lateinit var binding:ActivityDisplayfoodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDisplayfoodBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_displayfood)

        // Retrieve the product name from the intent
        val productName = intent.getStringExtra("productName")
        val brandName = intent.getStringExtra("brand")
        val servingSize = intent.getStringExtra("servingSize")
        var energyValue = intent.getDoubleExtra("energyValue", 0.0)
        var energyUnit = intent.getStringExtra("energyUnit")
        val allergens = intent.getStringExtra("allergens")
        val ingredients = intent.getStringExtra("ingredients")
        val imageString = intent.getStringExtra("imageUrl")

        val productNametv = findViewById<TextView>(R.id.productNametxt)
        productNametv.setText(productName)

        val brandNametv = findViewById<TextView>(R.id.brandNametxt)
        brandNametv.setText(brandName)

        val servingsizetv = findViewById<TextView>(R.id.servingsizetxt)
        servingsizetv.setText("Calories Per Serving: $servingSize")

        // Use regular expression to extract the numeric part
        val regex = Regex("""(\d+(\.\d+)?)""")
        val matchResult = servingSize?.let { regex.find(it) }

// Check if a match is found and extract the numeric part
        val numericPart = matchResult?.value

// Convert the numeric part to a Double
        val servingSizeNumeric = numericPart?.toDoubleOrNull()
        val initialServingSize = servingSizeNumeric

        if(energyUnit.equals("kJ")){
            val conversionFactor = 0.239005736
            energyValue = energyValue * conversionFactor
            energyUnit = "kcal"
        }

      val caloriesperserving = (energyValue/100) * servingSizeNumeric!!

        val caloriestv = findViewById<TextView>(R.id.productcaloriestv)
        caloriestv.setText(caloriesperserving.roundToInt().toString() + " " + energyUnit)

        val ingredientstv = findViewById<TextView>(R.id.productIngredienttv)
        ingredientstv.setText(ingredients)

        val allergiestv = findViewById<TextView>(R.id.productalergiestv)
        allergiestv.setText(allergens)


        val numberPicker = findViewById<NumberPicker>(R.id.dialogNumberPicker)
        numberPicker.minValue = 1
        numberPicker.maxValue = 999

        numberPicker.setOnValueChangedListener { _, _, newVal ->
            if (initialServingSize != null) {
                updateTotalCalories(initialServingSize, newVal.toDouble(), caloriesperserving)
            }
        }

// Set initial total calories
        if (energyValue != 0.0) {
            if (servingSizeNumeric != null) {
                numberPicker.value = servingSizeNumeric.roundToInt()
            }
            if (initialServingSize != null) {
                updateTotalCalories(initialServingSize, servingSizeNumeric, caloriesperserving)
            }
        } else {
            val cardView = findViewById<CardView>(R.id.myAmountCV)
            cardView.visibility = View.GONE
        }


        val imageView: ImageView = findViewById(R.id.productimgv)
        val imageUrl = imageString
        // Load image using Picasso
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.unavaliableimg) // Placeholder image while loading
            .error(R.drawable.unavaliableimg) // Error image if loading fails
            .into(imageView)



        val add = findViewById<Button>(R.id.addbtn)
        add.setOnClickListener(){
            val calories = findViewById<TextView>(R.id.totalCaloriestxt)
            val intent = Intent(this@displayfood, AddEnergy::class.java)
            intent.putExtra("calories", calories.text.toString().toInt())
            startActivity(intent)
        }


    }


    private fun updateTotalCalories(initialServingSize: Double,servingSize: Double, energyValue: Double) {
        val totalCalories = ((energyValue / initialServingSize) * servingSize).roundToInt()
        val totalCaloriesTextView = findViewById<TextView>(R.id.totalCaloriestxt)
        totalCaloriesTextView.text = totalCalories.toString()
    }

}