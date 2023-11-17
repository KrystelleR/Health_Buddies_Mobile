package com.varsitycollege.xbcad.healthbuddies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView

class advancedvideos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advancedvideos)

        val myvideo1 = findViewById<WebView>(R.id.video1)
        val myvideo2 = findViewById<WebView>(R.id.video2)
        val myvideo3 = findViewById<WebView>(R.id.video3)
        val myvideo4 = findViewById<WebView>(R.id.video4)
        val myvideo5 = findViewById<WebView>(R.id.video5)
        val myvideo6 = findViewById<WebView>(R.id.video6)
        val myvideo7 = findViewById<WebView>(R.id.video7)
        val myvideo8 = findViewById<WebView>(R.id.video8)

        val video1 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/GbjtG26ZSAo?si=LrUcZWuowUV-cSav\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video2 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/W-1bDW1ujsE?si=LHpsvCanBm16gfQd\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video3 ="<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/6e2ibUq65tA?si=ym3s0KCleA5g5UUm\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video4 ="<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/5F3o8iMQ_Lo?si=ZUuwnCWv6_jb7pBq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video5 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/WpIFlh5whcs?si=flFkARoXaSbboCX7\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video6 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/lc1Ag9m7XQo?si=fBU_XEi0z-g6lGjx\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video7 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/m4GrRPbrb3Y?si=ASK10WoN4keDONFP\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
        val video8 ="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/FNFYZ2n90RI?si=l0-EjpYhXd8ikhjB\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"


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