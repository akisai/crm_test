package com.example.haimin_a.crm_test.nav_fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.SignInActivity
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
import org.jetbrains.anko.startActivity
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
        setFields(view)
        return view
    }

    private fun setListListener(gson: ArrayList<MyTasks>) {
        list_tasks.setOnItemClickListener { _, _, position, _ ->
            deleteRasp(gson[position].id)
        }
    }

    private fun setFields(view: View) {
        getMyTasks()
        if (activity!!.intent.getStringExtra("type") == "google") {
            view.findViewById<TextView>(R.id.delete_acc).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.delete_acc).setOnClickListener {
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
                if (processingResponse(context!!, response, needSecond = false)) {
                    val gson: ArrayList<MyTasks> = GSON.fromJson(response, object : TypeToken<List<MyTasks>>() {}.type)
                    if (gson.isNotEmpty()) {
                        val tasks = ArrayList<String>()
                        for (t in gson) {
                            val str =
                                "Вы записаны к: " + t.name + " " + t.surname + "\nНа " + t.time + "    " + t.date + "\n" + "На процедуру " + t.procedure + "\nCтоимостью: " + t.cost.toString() + "\u20BD"
                            tasks.add(str)
                        }
                        list_tasks.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.list_item_tasks, tasks)
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
                    activity!!.getSharedPreferences("login", Context.MODE_PRIVATE)
                        .edit().putBoolean("logged", false).apply()
                    activity!!.startActivity<SignInActivity>()
                    activity!!.finish()
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
                    refresh()
                }
            }
        }
    }

    private fun refresh() {
        activity!!.supportFragmentManager.beginTransaction().detach(this@SettingsFragment).attach(this@SettingsFragment)
            .commit()
    }

}
