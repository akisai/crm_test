package com.example.haimin_a.crm_test.utils

import android.content.Context
import com.example.haimin_a.crm_test.rest_client.User
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import com.example.haimin_a.crm_test.SignInActivity
import org.jetbrains.anko.*

fun buildPostParams(connection: HttpURLConnection) {
    connection.requestMethod = "POST"
    connection.connectTimeout = 30000
    connection.readTimeout = 30000
    connection.doOutput = true
    connection.doInput = true
    connection.setRequestProperty("Content-Type", "application/json")
}

fun getPostResponse(url: String, json: String): String {
    val response: String?
    try {
        val connection = URL(url)
            .openConnection() as HttpURLConnection
        buildPostParams(connection)
        connection.outputStream.write(json.toByteArray())
        response = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
    } catch (e: Throwable) {
        return true.toString()
    }
    return response
}

fun processingResponse(
    context: Context,
    response: String,
    titleAllert: String = "Error",
    message2: String = "Unexpected error",
    message1: String = "Connection error"
): Boolean {
    if (response.toBoolean()) {
        context.alert(message1) {
            title = titleAllert
            yesButton {}
        }.show()
    } else {
        if (!response.isEmpty()) {
            return true
        } else
            context.alert(message2) {
                title = titleAllert
                yesButton {}
            }.show()
    }
    return false
}
