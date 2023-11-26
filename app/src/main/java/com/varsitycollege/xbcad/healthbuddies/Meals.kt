package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.varsitycollege.xbcad.healthbuddies.R

class Meals : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_meals)

        //nav to recipe page -15 buttons - 15 recipes (pull from db in recipe.kt)


        //BREAKFAST MEALS

        //breakfast1--mushroom omelet
        val btnB1 =findViewById<ImageButton>(R.id.btn_breakfast1)
        btnB1.setOnClickListener {
            val intent =Intent(this,Recipe::class.java)
            intent.putExtra("MEAL_NAME","Mushroom Omelet")
            intent.putExtra("MEAL_TYPE","breakfast")

            startActivity(intent)
        }

        // breakfast 2 muesli
        val btnB2: ImageButton = findViewById(R.id.btn_breakfast2)
        btnB2.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "Muesli with Raspberry")
            intent.putExtra("MEAL_TYPE", "breakfast")

            startActivity(intent)
        }

        //breakfast3 ham cheese eggs
        val btnB3: ImageButton = findViewById(R.id.btn_breakfast3)
        btnB3.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "Ham and Cheese Eggs")
            intent.putExtra("MEAL_TYPE", "breakfast")

            startActivity(intent)
        }

        //breakfast4 peanut butter and banana
        val btnB4: ImageButton = findViewById(R.id.btn_breakfast4)
        btnB4.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","PB and Banana Sandwich")
            intent.putExtra("MEAL_TYPE","breakfast")

            startActivity(intent)
        }

        //breakfast5- oats
        val btnB5: ImageButton = findViewById(R.id.btn_breakfast5)
        btnB5.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "Oatmeal")
            intent.putExtra("MEAL_TYPE", "breakfast")

            startActivity(intent)
        }



        //LUNCH MEALS

        //lunch1--Sweet Potato mac & Cheese
        val btnL1: ImageButton = findViewById(R.id.btn_lunch1)
        btnL1.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Sweet Potato mac & Cheese")
            intent.putExtra("MEAL_TYPE","lunch")

            startActivity(intent)
        }

        //lunch2--Fish and Chips
        val btnL2: ImageButton = findViewById(R.id.btn_lunch2)
        btnL2.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "Fish and Chips")
            intent.putExtra("MEAL_TYPE", "lunch")

            startActivity(intent)
        }

        //lunch3--BLT Wraps
        val btnL3: ImageButton = findViewById(R.id.btn_lunch3)
        btnL3.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "BLT Wraps")
            intent.putExtra("MEAL_TYPE", "lunch")

            startActivity(intent)
        }

        //lunch4--BBQ Chicken Pizza
        val btnL4: ImageButton = findViewById(R.id.btn_lunch4)
        btnL4.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "BBQ Chicken Pizza")
            intent.putExtra("MEAL_TYPE", "lunch")

            startActivity(intent)
        }

        //lunch5--Classic Hamburger
        val btnL5: ImageButton = findViewById(R.id.btn_lunch5)
        btnL5.setOnClickListener {
            val intent = Intent(this@Meals, Recipe::class.java)
            intent.putExtra("MEAL_NAME", "Classic Hamburger")
            intent.putExtra("MEAL_TYPE", "lunch")

            startActivity(intent)
        }


        //DINNER MEALS

        //dinner1--Lasagna
        val btnD1: ImageButton = findViewById(R.id.btn_dinner1)
        btnD1.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Lasagna")
            intent.putExtra("MEAL_TYPE","dinner")

            startActivity(intent)
        }

        //dinner2--Chicken Fried Rice
        val btnD2: ImageButton = findViewById(R.id.btn_dinner2)
        btnD2.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Chicken Fried Rice")
            intent.putExtra("MEAL_TYPE","dinner")

            startActivity(intent)
        }

        //dinner3--Creamy Mac & Cheese
        val btnD3: ImageButton = findViewById(R.id.btn_dinner3)
        btnD3.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Creamy Mac & Cheese")
            intent.putExtra("MEAL_TYPE","dinner")

            startActivity(intent)
        }

        //dinner4--Chicken Pasta
        val btnD4: ImageButton = findViewById(R.id.btn_dinner4)
        btnD4.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Chicken Pasta")
            intent.putExtra("MEAL_TYPE","dinner")

            startActivity(intent)
        }

        //dinner5--Vegetable Pasta
        val btnD5: ImageButton = findViewById(R.id.btn_dinner5)
        btnD5.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Vegetable Pasta")
            intent.putExtra("MEAL_TYPE","dinner")

            startActivity(intent)
        }


        //SNACKS

        //snacks1--PB Choc Chip Cookie
        val btnS1: ImageButton = findViewById(R.id.btn_snacks1)
        btnS1.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","PB Choc Chip Cookie")
            intent.putExtra("MEAL_TYPE","snacks")

            startActivity(intent)
        }

        //snacks2--Whipped Frozen Lemonade
        val btnS2: ImageButton = findViewById(R.id.btn_snacks2)
        btnS2.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Whipped Frozen Lemonade")
            intent.putExtra("MEAL_TYPE","snacks")

            startActivity(intent)
        }

        //snacks3--Strawberry NICE Cream
        val btnS3: ImageButton = findViewById(R.id.btn_snacks3)
        btnS3.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Strawberry NICE Cream")
            intent.putExtra("MEAL_TYPE","snacks")

            startActivity(intent)
        }

        //snacks4--Microwave Popcorn
        val btnS4: ImageButton = findViewById(R.id.btn_snacks4)
        btnS4.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Microwave Popcorn")
            intent.putExtra("MEAL_TYPE","snacks")

            startActivity(intent)
        }

        //snacks5--Fruit Salad
        val btnS5: ImageButton = findViewById(R.id.btn_snacks5)
        btnS5.setOnClickListener {
            val intent = Intent(this, Recipe::class.java)
            intent.putExtra("MEAL_NAME","Fruit Salad")
            intent.putExtra("MEAL_TYPE","snacks")

            startActivity(intent)
        }


    }

    //if back button is pressed then navigate to nutrition activity
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Nutrition::class.java)
        startActivity(intent)
        finish()
    }
}