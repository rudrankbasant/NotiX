package com.dscvit.notix.utils

import android.content.Context
import android.util.Log
import com.dscvit.notix.utils.Constants.SPAM_POINT
import org.tensorflow.lite.support.label.Category

object SpamClassifier {
    fun spamClassifyMessage(context: Context, message: String) : Boolean {
        // Loading the ML model for spam detection
        val client = SpamClassificationClient(context)
        client.load()

        // Predicts if the notification is a spam notification
        var isSpam = false
        if (message.trim() != "") {
            val results: List<Category> = client.classify(message)
            val score = results[1].score
            Log.d("SPAM_SCORE", score.toString())
            if (score > SPAM_POINT) {
                isSpam = true
            }
        }
        return isSpam
    }
}