package com.example.haimin_a.crm_test.rest_client

data class User(val login: String, val password: String, val registration_date: String, val id: Long)

data class SaveUser(val login: String, val password: String, val registration_date: String)

data class FindUser(val login: String, val password: String)