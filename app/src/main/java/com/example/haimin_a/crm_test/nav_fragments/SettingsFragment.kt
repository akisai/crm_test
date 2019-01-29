package com.example.haimin_a.crm_test.nav_fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.rest_client.FindMyTask
import com.example.haimin_a.crm_test.rest_client.MyTasks
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.UserId
import com.example.haimin_a.crm_test.utils.getPostResponse
import com.example.haimin_a.crm_test.utils.processingResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_settings.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread


class SettingsFragment : Fragment() {

    lateinit var GSON: Gson
    lateinit var REST_URL: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        REST_URL = getString(R.string.rest_url)
        GSON = Gson()
        view.findViewById<TextView>(R.id.rasp_text).text = getString(R.string.yours_procedure)
        setFields()
        return view
    }

    private fun setListListener(gson: ArrayList<MyTasks>) {
        list_tasks.setOnItemClickListener { _, _, position, _ ->
            deleteRasp(gson[position].id)
        }
    }

    private fun setFields() {
        getMyTasks()
        if (activity!!.intent.getStringExtra("type") == "google") {
            delete_acc.visibility = View.GONE
        } else {
            delete_acc.setOnClickListener {
                deleteAcc()
            }
        }
    }

    private fun getMyTasks() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get yours tasks...")
        dialogLog.setCancelable(false)
        doAsync {
            val json = GSON.toJson(FindMyTask(activity!!.intent.getStringExtra("user").toLong()))
            val response = getPostResponse(REST_URL + Operations.getMyRasp.str, json)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(activity!!, response, needSecond = false)) {
                    val gson: ArrayList<MyTasks> = GSON.fromJson(response, object : TypeToken<List<MyTasks>>() {}.type)
                    val tasks = ArrayList<String>()
                    for (t in gson) {
                        val str =
                            "Вы записаны к: " + t.name + " " + t.surname + " на " + t.time + " " + t.date + "\n" + "На процедуру " + t.name + "стоимостью: " + t.cost.toString() + "\u20BD"
                        tasks.add(str)
                    }
                    list_tasks.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.time_list, tasks)
                    rasp_info.visibility = View.GONE
                    delete_rasp.visibility = View.VISIBLE
                    list_tasks.visibility = View.VISIBLE
                    delete_rasp.setOnClickListener {
                        setListListener(gson)
                    }
                }
            }
        }
    }

    private fun deleteAcc() {
        val dialogLog = activity!!.indeterminateProgressDialog("Deleting task from rasp...")
        dialogLog.setCancelable(false)
        doAsync {
            val json = GSON.toJson(UserId(activity!!.intent.getStringExtra("user").toLong()))
            val response = getPostResponse(REST_URL + Operations.deleteAcc.str, json)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(activity!!, response, needSecond = false)) {
                    fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    activity!!.getSharedPreferences("login", Context.MODE_PRIVATE)
                        .edit().putBoolean("logged", false).apply()
                    activity!!.onBackPressed()
                }
            }
        }
    }

    private fun deleteRasp(id: Long) {
        val dialogLog = activity!!.indeterminateProgressDialog("Deleting task from rasp...")
        dialogLog.setCancelable(false)
        doAsync {
            val json = GSON.toJson(FindMyTask(id))
            val response = getPostResponse(REST_URL + Operations.deleteRasp.str, json)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(activity!!, response, needSecond = false)) {

                }
            }
        }
    }
}
