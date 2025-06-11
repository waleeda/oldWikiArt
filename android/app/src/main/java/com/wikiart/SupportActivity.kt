package com.wikiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        findViewById<Button>(R.id.feedbackButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:wikiartfeedback@icloud.com")
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.donateButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.buymeacoffee.com/wikiart"))
            startActivity(intent)
        }
    }
}
