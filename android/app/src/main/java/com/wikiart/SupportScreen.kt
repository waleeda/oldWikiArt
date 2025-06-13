package com.wikiart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SupportScreen(
    onSendFeedback: () -> Unit,
    onDonate: () -> Unit
) {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSendFeedback
            ) {
                Text(text = stringResource(R.string.send_feedback))
            }
            Spacer(Modifier.height(12.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDonate
            ) {
                Text(text = stringResource(R.string.donate))
            }
        }
    }
}
