package com.wikiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ComposeSupportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupportScreen(
                onSendFeedback = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:wikiartfeedback@icloud.com")
                    }
                    startActivity(intent)
                },
                onDonate = { /* TODO: show billing options */ }
            )
        }
    }
}
