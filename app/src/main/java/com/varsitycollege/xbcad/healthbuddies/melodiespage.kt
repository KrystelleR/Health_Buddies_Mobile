package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class melodiespage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_melodiespage)


        // Find the WebViews by their IDs
        val webView1: WebView = findViewById(R.id.spotifyWebView1)
        val webView2: WebView = findViewById(R.id.spotifyWebView2)
        val webView3: WebView = findViewById(R.id.spotifyWebView3)

        // Enable JavaScript for the WebViews
        val webSettings1: WebSettings = webView1.settings
        val webSettings2: WebSettings = webView2.settings
        val webSettings3: WebSettings = webView3.settings
        webSettings1.javaScriptEnabled = true
        webSettings2.javaScriptEnabled = true
        webSettings3.javaScriptEnabled = true

        // Allow autoplay of audio (important for Spotify playlists)
        webSettings1.mediaPlaybackRequiresUserGesture = false
        webSettings2.mediaPlaybackRequiresUserGesture = false
        webSettings3.mediaPlaybackRequiresUserGesture = false

        // Load the Spotify playlist URLs
        val spotifyPlaylistURL1 = "https://open.spotify.com/embed/playlist/37i9dQZF1DX1Ew8a92fTdm?utm_source=generator"
        val spotifyPlaylistURL2 = "https://open.spotify.com/embed/playlist/37i9dQZF1DWUZ5bk6qqDSy?utm_source=generator"
        val spotifyPlaylistURL3 = "https://open.spotify.com/embed/playlist/37i9dQZF1DX0zmsulfyDdq?utm_source=generator"

        webView1.loadUrl(spotifyPlaylistURL1)
        webView2.loadUrl(spotifyPlaylistURL2)
        webView3.loadUrl(spotifyPlaylistURL3)

        // Set WebViewClients with custom URL handling
        webView1.webViewClient = createWebViewClient()
        webView2.webViewClient = createWebViewClient()
        webView3.webViewClient = createWebViewClient()
    }

    private fun createWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Check if the URL is for an external site (not part of the WebView)
                if (url != null && !url.startsWith("https://open.spotify.com/")) {
                    // Check if the URL is a Spotify link
                    if (url.startsWith("https://open.spotify.com/")) {
                        // Open the Spotify app for Spotify links
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        intent.setPackage("com.spotify.music") // Package name for the Spotify app
                        startActivity(intent)
                        return true
                    } else {
                        // Open the URL in an external browser for non-Spotify links
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }
                }
                return false // Allow internal WebView links to load normally
            }
        }
    }
}