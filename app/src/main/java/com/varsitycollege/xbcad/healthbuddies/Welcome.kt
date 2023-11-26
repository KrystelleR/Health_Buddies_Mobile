package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var iv1: CardView
private lateinit var iv2: CardView
private lateinit var iv3: CardView
private lateinit var iv4: CardView
private lateinit var iv5: CardView
private lateinit var iv6: CardView

private lateinit var headertxt: TextView
private lateinit var paragraphtxt: TextView

private lateinit var viewPager2: ViewPager2

private lateinit var auth: FirebaseAuth

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        auth= Firebase.auth

        setContentView(R.layout.activity_welcome)

        val signUp = findViewById<Button>(R.id.signUpbtn)

        signUp.setOnClickListener(){
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val signIn = findViewById<Button>(R.id.signInbtn)

        signIn.setOnClickListener(){
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        viewPager2= findViewById(R.id.viewPager2)
        iv1 = findViewById(R.id.im1)
        iv2 = findViewById(R.id.im2)
        iv3 = findViewById(R.id.im3)
        iv4 = findViewById(R.id.im4)
        iv5 = findViewById(R.id.im5)
        iv6 = findViewById(R.id.im6)

        headertxt = findViewById(R.id.headerstxt)
        paragraphtxt = findViewById(R.id.paragraphtxt)

        val images = listOf(R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6)
        val adapter = ViewPagerAdapter(images)
        viewPager2.adapter = adapter

        //chnaging color of dot indicater when image is on current image
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                changeColor()
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, Welcome::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUI(){
        val Intent = Intent(this, MainActivity::class.java)
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



    fun changeColor(){
        when (viewPager2.currentItem)
        {
            0->
            {
                headertxt.setText("Fitness Fun")
                paragraphtxt.setText("Step into a world of movement and joy! Our step counter keeps kids active, while fun exercise videos make staying fit an adventure.")
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            1->
            {
                headertxt.setText("Nutrition Explorer")
                paragraphtxt.setText("Our nutrition page offers a variety of meals, and with the food barcode scanner, children can learn about the goodness in every bite.")
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            2->
            {
                headertxt.setText("Avatar App Store")
                paragraphtxt.setText("Dive into a world of personalization! The Avatar App Store lets kids express themselves with unique avatars.")
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            3->
            {
                headertxt.setText("Hydration Hero")
                paragraphtxt.setText("Stay refreshed, stay strong! Our water tracker helps kids maintain the right balance.")
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            4->
            {
                headertxt.setText("Dreamland Diary")
                paragraphtxt.setText("The sleep tracker ensures a good night's rest, making sure Health Buddies wake up refreshed and ready for a new day of learning.")
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }

            5->
            {
                headertxt.setText("Leaderboard Legend")
                paragraphtxt.setText("Challenge accepted! Climb the ranks and become a Health Buddies Legend on our leaderboard.")
                iv6.setCardBackgroundColor(applicationContext.resources.getColor(R.color.orange))
                iv2.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv3.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv4.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv5.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
                iv1.setCardBackgroundColor(applicationContext.resources.getColor(R.color.white))
            }
        }
    }
}