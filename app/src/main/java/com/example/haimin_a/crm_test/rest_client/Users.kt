package com.example.haimin_a.crm_test.rest_client

data class User(val login: String, val password: String, val registration_date: String, val id: Long)

data class SaveUser(val login: String, val password: String, val registration_date: String)

data class FindUser(val login: String, val password: String)

data class FindUserInfo(val user_id: Long)

data class UserInfo(val category: String, val userId: Long, val name: String?, val surname: String?, val patronymic: String?, val birthday: String?, val email: String?, val pic: String?, val id: Long)

data class DoctorsInfo(val name: String, val surname: String, val birthday: String, val procedure: String, val start: String, val end: String, val userId: Long, val id: Long)

data class ProcedureInfo(val id: Long, val name: String, val description: String, val cost: Long, val doctors: String)

data class FindTimeInfo(val id: Long)

data class TimeInfo(val id: Long, val start: String, val end: String)

data class FindTasks(val doctorId: Long, val date: String)

data class TasksInfo(val userId: Long, val doctorId: Long, val procedureId: Long, val time: String, val date: String, val id: Long)

data class SaveRasp(val userId: Long, val doctorId: Long, val procedureId: Long, val time: String, val date: String)

data class FindMyTask(val uId: Long)

data class UserId(val id: Long)

data class MyTasks(val id: Long, val time: String, val date: String, val name: String, val surname: String, val procedure: String, val description: String, val cost: Long)
