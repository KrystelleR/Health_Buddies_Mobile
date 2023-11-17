package com.varsitycollege.xbcad.healthbuddies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView

class intermediatevideos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intermediatevideos)

        val myvideo1 = findViewById<WebView>(R.id.video1)
        val myvideo2 = findViewById<WebView>(R.id.video2)
        val myvideo3 = findViewById<WebView>(R.id.video3)
        val myvideo4 = findViewById<WebView>(R.id.video4)
        val myvideo5 = findViewById<WebView>(R.id.video5)
        val myvideo6 = findViewById<WebView>(R.id.video6)
        val myvideo7 = findViewById<WebView>(R.id.video7)
        val myvideo8 = findViewById<WebView>(R.id.video8)

        val video1 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/KnF12nKwPQI?si=u1IAojfQJtVymx5m\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video2 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/N1ZnrTXJvh0?si=t0DDE7RPfdZtIHKu\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video3 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/JWTyO8npkOQ?si=t8IhqMBoSH77P48F\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video4 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/h3DSYn2jIKE?si=3DpBxaYdol2iJ33O\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video5 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/L_A_HjHZxfI?si=4uKJQO1KlvVE3_G0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video6 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/8gCviKT8UdQ?si=FhLgm3dAMMtbG_cq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video7 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/IgrkxDmIby8?si=oVhHpivEQqMCMSof\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video8 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/t7nrOBBfcYI?si=xQYAJ7pNY5d65wBe\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"


        myvideo1.loadData(video1, "text/html", "utf-8")
        myvideo1.settings.javaScriptEnabled = true
        myvideo1.webChromeClient = WebChromeClient()

        myvideo2.loadData(video2, "text/html", "utf-8")
        myvideo2.settings.javaScriptEnabled = true
        myvideo2.webChromeClient = WebChromeClient()

        myvideo3.loadData(video3, "text/html", "utf-8")
        myvideo3.settings.javaScriptEnabled = true
        myvideo3.webChromeClient = WebChromeClient()

        myvideo4.loadData(video4, "text/html", "utf-8")
        myvideo4.settings.javaScriptEnabled = true
        myvideo4.webChromeClient = WebChromeClient()

        myvideo5.loadData(video5, "text/html", "utf-8")
        myvideo5.settings.javaScriptEnabled = true
        myvideo5.webChromeClient = WebChromeClient()

        myvideo6.loadData(video6, "text/html", "utf-8")
        myvideo6.settings.javaScriptEnabled = true
        myvideo6.webChromeClient = WebChromeClient()

        myvideo7.loadData(video7, "text/html", "utf-8")
        myvideo7.settings.javaScriptEnabled = true
        myvideo7.webChromeClient = WebChromeClient()

        myvideo8.loadData(video8, "text/html", "utf-8")
        myvideo8.settings.javaScriptEnabled = true
        myvideo8.webChromeClient = WebChromeClient()
    }
}