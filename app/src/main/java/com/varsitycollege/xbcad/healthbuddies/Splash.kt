package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {

    // This is the delay time in milliseconds
    private val SPLASH_DELAY: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        // Use a Handler to delay the transition
        Handler().postDelayed({
            if (isUserLoggedIn()) {
                val intent = Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@Splash, Welcome::class.java)
                startActivity(intent)
            }

            // Close the current activity
            finish()
        }, SPLASH_DELAY)
    }

    private fun isUserLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        return auth.currentUser != null
    }
}
