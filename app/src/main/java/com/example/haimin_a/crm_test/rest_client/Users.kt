package com.example.haimin_a.crm_test.rest_client

data class User(val login: String, val password: String, val registration_date: String, val id: Long)

data class SaveUser(val login: String, val password: String, val registration_date: String)

data class FindUser(val login: String, val password: String)

data class FindUserInfo(val user_id: Long)

data class UserInfo(val category: String, val userId: Long, val name: String, val surname: String, val patronymic: String, val birthday: String, val email: String, val pic: String, val id: Long)

data class DoctorsInfo(val name: String, val surname: String, val birthday: String, val procedure: String, val start: String, val end: String)