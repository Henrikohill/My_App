package com.example.myapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.SearchView
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val bASE_URL = "https://m.youtube.com"
        val sEARCH_PATH = "/results?search_query="
        //Refresh
        swipeRefresh.setOnRefreshListener {
            webView.reload()
        }

        //search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {

                p0?.let {
                    if (URLUtil.isValidUrl(it)) {
                        //es url
                        webView.loadUrl(it)
                    } else {
                        //no es url
                        if (it.indexOf("")>=0) {
                            var ti = it.replace("\\s".toRegex(), "+")
                            webView.loadUrl("$bASE_URL$sEARCH_PATH$ti")
                        } else {
                            webView.loadUrl("$bASE_URL$sEARCH_PATH$it")
                        }
                    }
                }
                return false
            }

        })

        // webview
        webView.webChromeClient = object : WebChromeClient() {

        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                //searchView.setQuery(url, false)

                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                swipeRefresh.isRefreshing = false
            }
        }
        val setting: WebSettings = webView.settings
        setting.javaScriptEnabled = true
        webView.loadUrl(bASE_URL)

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}