package com.example.haimin_a.crm_test.nav_fragments.core


import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.rest_client.*
import com.example.haimin_a.crm_test.utils.getPostResponse
import com.example.haimin_a.crm_test.utils.processingResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_time.*
import org.jetbrains.anko.*
import java.time.LocalTime
import java.util.*


class TimeFragment : Fragment() {

    private var task: ArrayList<String>? = null
    private lateinit var REST_URL: String
    private lateinit var GSON: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            task = arguments!!.getStringArrayList(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time, container, false)
        GSON = Gson()
        REST_URL = getString(R.string.rest_url)
        setFields()
        return view
    }


    private fun setFields() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
            activity!!.findViewById<TextView>(R.id.dateDoctor).text = DateFormat.format("yyyy-MM-dd", cal.time)
            makeTime()
        }
        val cancelListener = DialogInterface.OnCancelListener {
            activity!!.alert("You must select a date") {
                title = "Attention"
                yesButton {
                    activity!!.supportFragmentManager.beginTransaction().detach(this@TimeFragment)
                        .attach(this@TimeFragment).commit()
                }
                noButton {
                    fragmentManager!!.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }.show()
        }
        val datePickerDialog = DatePickerDialog(
            activity!!,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = cal.timeInMillis
        datePickerDialog.setOnCancelListener(cancelListener)
        datePickerDialog.show()

    }


    private fun makeTime() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get time info...", "")
        dialogLog.setCancelable(false)
        doAsync {
            val json = GSON.toJson(FindTimeInfo(task!![0].toLong()))
            val jsonTime = GSON.toJson(FindTasks(task!![0].toLong(), dateDoctor.text.toString()))
            val response = getPostResponse(REST_URL + Operations.getTime.str, json)
            val responseTime = getPostResponse(REST_URL + Operations.getRasp.str, jsonTime)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(activity!!, response, needFirst = false, needSecond = false)) {
                    val wrongTime: ArrayList<LocalTime> = ArrayList()
                    if (processingResponse(activity!!, responseTime, needFirst = false, needSecond = false)) {
                        val gsonTime: ArrayList<TasksInfo> =
                            GSON.fromJson(responseTime, object : TypeToken<List<TasksInfo>>() {}.type)
                        for (t in gsonTime) {
                            wrongTime.add(LocalTime.parse(t.time))
                        }
                    }
                    val gson: TimeInfo = GSON.fromJson(response, TimeInfo::class.java)
                    val time: ArrayList<LocalTime> = ArrayList()
                    var rasp = LocalTime.parse(gson.start)
                    val end = LocalTime.parse(gson.end)
                    do {
                        if (!wrongTime.contains(rasp))
                            time.add(rasp)
                        rasp = rasp.plusMinutes(30)
                    } while (end > rasp)
                    list_time.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.time_list, time)
                    list_time.setOnItemClickListener { _, _, position, _ ->
                        saveRasp(time[position])
                    }
                } else {
                    activity!!.alert("Connection error") {
                        title = "Get time failed"
                        yesButton {
                            activity!!.supportFragmentManager.popBackStack()
                        }
                    }.show()
                }
            }
        }
    }

    private fun saveRasp(time: LocalTime) {
        val dialogSave = activity!!.indeterminateProgressDialog("Save rasp...", "")
        dialogSave.setCancelable(false)
        doAsync {
            val json = GSON.toJson(
                SaveRasp(
                    activity!!.intent.getStringExtra("user").toLong(),
                    task!![0].toLong(),
                    task!![1].toLong(),
                    time.toString(),
                    dateDoctor.text.toString()
                )
            )
            val responseRasp = getPostResponse(REST_URL + Operations.saveRasp.str, json)
            uiThread {
                Thread.sleep(500)
                dialogSave.dismiss()
                if (processingResponse(activity!!, responseRasp)) {
                    getBack()
                }
            }
        }
    }

    private fun getBack() {
        activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        activity!!.alert("You can view your schedule in the settings") {
            title = "Success"
            yesButton {}
        }.show()
    }

    companion object {
        private val ARG_PARAM = "TaskInfo"

        fun newInstance(list: ArrayList<String>): TimeFragment {
            val fragment = TimeFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_PARAM, list)
            fragment.arguments = args
            return fragment
        }
    }

}



