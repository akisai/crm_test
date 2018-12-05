package com.example.haimin_a.crm_test


private var SEP: String = "/"

fun doNothing(): Boolean = false

fun buildURL(vararg strings: String): String {
    var result = ""
    for (str in strings)
        result += str + SEP
    return result
}