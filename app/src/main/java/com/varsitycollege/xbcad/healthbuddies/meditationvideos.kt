package com.varsitycollege.xbcad.healthbuddies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView

class meditationvideos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_meditationvideos)

        val myvideo1 = findViewById<WebView>(R.id.video1)
        val myvideo2 = findViewById<WebView>(R.id.video2)
        val myvideo3 = findViewById<WebView>(R.id.video3)
        val myvideo4 = findViewById<WebView>(R.id.video4)

        val video1 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/eKbfUtLoQwE?si=a73SJdRbBro_NQds\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video2 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/WhoIeqDJM6E?si=oxWkLAz13RlnjcsY\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video3 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/9hbSsuss3YA?si=oR3oBaC5qSlPJtrm\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video4 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ELOads7rbxE?si=4Te6nb8RV7k6nA8d\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"

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
    }
}