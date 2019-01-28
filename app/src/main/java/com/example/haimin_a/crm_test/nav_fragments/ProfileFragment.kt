package com.example.haimin_a.crm_test.nav_fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.rest_client.FindUserInfo
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.UserInfo
import com.example.haimin_a.crm_test.utils.DatePickerFragment
import com.example.haimin_a.crm_test.utils.getPostResponse
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_service.*
import org.jetbrains.anko.*

class ProfileFragment : Fragment() {

    var user_id: Long? = null
    var category: String? = null
    var pic: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service, container, false)
        val edit = view.findViewById<Button>(R.id.edit)
        edit.setOnClickListener {
            editFields()
        }
        setFields()
        return view
    }

    fun setFields() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get user info...")
        dialogLog.setCancelable(false)
        doAsync {
            val json = Gson().toJson(FindUserInfo(activity!!.intent.getStringExtra("user").toLong()))
            val response = getPostResponse(getString(R.string.rest_url) + Operations.userInfo.str, json)
            uiThread {
                dialogLog.dismiss()
                if (response.isNotEmpty()) {
                    val gson: UserInfo = Gson().fromJson(response, UserInfo::class.java)
                    if (gson.name.isNotEmpty()) name.text = gson.name
                    if (gson.surname.isNotEmpty()) surname.text = gson.surname
                    if (gson.patronymic.isNotEmpty()) patronymic.text = gson.patronymic
                    if (gson.birthday.isNotEmpty()) birthday.text = gson.birthday
                    if (gson.email.isNotEmpty()) email.text = gson.email
                    user_id = gson.userId
                    category = gson.category
                    pic = if (gson.pic.isNotEmpty()) gson.pic else getString(R.string.google_pic)
                    Picasso.get().load(pic).into(image)
                }
            }
        }
    }

    fun editFields() {
        edit.visibility = View.INVISIBLE
        name.setOnClickListener {
            name.visibility = View.INVISIBLE
            edit_name.visibility = View.VISIBLE
            edit_name.requestFocus()
        }
        surname.setOnClickListener {
            surname.visibility = View.INVISIBLE
            edit_surname.visibility = View.VISIBLE
            edit_surname.requestFocus()
        }
        patronymic.setOnClickListener {
            patronymic.visibility = View.INVISIBLE
            edit_patronymic.visibility = View.VISIBLE
            edit_patronymic.requestFocus()
        }
        birthday.setOnClickListener {
            val fragment = DatePickerFragment()
            fragment.show(fragmentManager, "Date Picker")
        }
        email.setOnClickListener {
            email.visibility = View.INVISIBLE
            edit_email.visibility = View.VISIBLE
            edit_email.requestFocus()
        }
        save.visibility = View.VISIBLE
        save.setOnClickListener {
            saveFields()
        }
    }

    private fun saveFields() {
        val dialogLog = activity!!.indeterminateProgressDialog("Save user info...")
        dialogLog.setCancelable(false)
        doAsync {
            val json = Gson().toJson(
                UserInfo(
                    if (category.isNullOrEmpty()) "user" else category!!,
                    user_id!!,
                    if (edit_name.text.isNotEmpty()) edit_name.text.toString() else name.text.toString(),
                    if (edit_surname.text.isNotEmpty()) edit_surname.text.toString() else surname.text.toString(),
                    if (edit_patronymic.text.isNotEmpty()) edit_patronymic.text.toString() else patronymic.text.toString(),
                    birthday.text.toString(),
                    if (edit_email.text.isNotEmpty()) edit_email.text.toString() else email.text.toString(),
                    pic!!,
                    activity!!.intent.getStringExtra("user").toLong()
                )
            )
            val response = getPostResponse(getString(R.string.rest_url) + Operations.updateInfo.str, json)
            uiThread {
                dialogLog.dismiss()
                if (response.isEmpty() || response.toBoolean()) {
                    activity!!.alert("Error") {
                        title = "Save failed"
                        yesButton {
                            refresh()
                        }
                    }.show()
                } else {
                    refresh()
                }
            }
        }
    }

    private fun refresh() {
        activity!!.supportFragmentManager.beginTransaction().detach(this@ProfileFragment).attach(this@ProfileFragment).commit()
    }
}




