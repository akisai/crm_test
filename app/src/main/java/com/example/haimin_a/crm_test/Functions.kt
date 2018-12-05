package com.example.haimin_a.crm_test

import java.net.HttpURLConnection


private var SEP: String = "/"

fun doNothing(): Boolean = false

fun buildURL(vararg strings: String): String {
    var result = ""
    for (str in strings)
        result += str + SEP
    return result
}

fun buildPostParam(connection: HttpURLConnection) {
    connection.requestMethod = "POST"
    connection.connectTimeout = 30000
    connection.readTimeout = 30000
    connection.doOutput = true
    connection.doInput = true
    connection.setRequestProperty("Content-Type", "application/json")
}