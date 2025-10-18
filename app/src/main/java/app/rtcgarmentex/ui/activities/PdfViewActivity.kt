package app.rtcgarmentex.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import app.rtcgarmentex.databinding.ActivityPdfViewBinding
import app.rtcgarmentex.utils.ToastHelper

class PdfViewActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityPdfViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        mBinding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val client = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                mBinding.progressbar.visibility = VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mBinding.progressbar.visibility = GONE
            }
        }

        mBinding.webView.webViewClient = client
        mBinding.webView.settings.setSupportZoom(true)
        mBinding.webView.settings.javaScriptEnabled = true
        val url = intent.getStringExtra("url")
        ToastHelper.showSnackBar(mBinding.root, "Loading... Please wait")
        mBinding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
//        mBinding.webView.loadUrl(Uri.parse(url).toString())

        mBinding.backIv.setOnClickListener {
            onBackPressed()
        }
    }
}