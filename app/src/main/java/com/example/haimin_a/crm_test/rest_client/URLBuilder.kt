package com.example.haimin_a.crm_test.rest_client

object URLBuilder {

    private var SEP: String = "/"
    fun build(vararg strings: String): String {
        var result = ""
        for (str in strings)
            result += str + SEP
        return result
    }
}