package com.dscvit.notix.utils

import android.content.Context
import android.util.Log
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier
import java.io.IOException

class SpamClassificationClient(private val context: Context) {
    var classifier: NLClassifier? = null
    fun load() {
        try {
            classifier = NLClassifier.createFromFile(context, MODEL_PATH)
        } catch (e: IOException) {
            Log.e(TAG, e.message!!)
        }
    }

    fun unload() {
        classifier!!.close()
        classifier = null
    }

    fun classify(text: String?): List<Category> {
        return classifier!!.classify(text)
    }

    companion object {
        private const val MODEL_PATH = "model.tflite"
        private const val TAG = "NotificationSpam"
    }
}
