package com.example.haimin_a.crm_test.nav_fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.rest_client.*
import com.example.haimin_a.crm_test.utils.getPostResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_time.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class TimeFragment : Fragment() {

    private var docId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            docId = arguments!!.getString(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_time, container, false)
        setFields()
        return view
    }


    fun setFields() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
            activity!!.findViewById<TextView>(R.id.dateDoctor).text = DateFormat.format("yyyy-MM-dd", cal.time)
            makeTime()
        }
        DatePickerDialog(
            activity!!,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()

    }


    fun makeTime() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get time info...", "")
        dialogLog.setCancelable(false)
        doAsync {
            val json = Gson().toJson(FindTimeInfo(docId!!.toLong()))
            val jsonTime = Gson().toJson(FindTasks(docId!!.toLong(), LocalDate.parse(dateDoctor.text)))
            val response = getPostResponse(getString(R.string.rest_url) + Operations.getTime.str, json)
            val responseTime = getPostResponse(getString(R.string.rest_url) + Operations.getTime.str, jsonTime)
            uiThread {
                dialogLog.dismiss()
                if (response.isNotEmpty()) {
                    val wrongTime: ArrayList<LocalTime> = ArrayList()
                    if (responseTime.isNotEmpty()){
                        val gsonTime : ArrayList<TasksInfo> =
                        Gson().fromJson(response, object : TypeToken<List<TasksInfo>>() {}.type)
                        for (t in gsonTime) {
                            wrongTime.add(LocalTime.parse(t.time))
                        }
                    }
                    val gson: TimeInfo = Gson().fromJson(response, TimeInfo::class.java)
                    val time: ArrayList<LocalTime> = ArrayList()
                    var rasp = LocalTime.parse(gson.start)
                    val end = LocalTime.parse(gson.end)
                    for (i in 0..100) {
                        if (!wrongTime.contains(rasp)) {
                            time.add(rasp)
                            if (end > time[i].plusMinutes(30))
                                rasp = rasp.plusMinutes(30)
                            else
                                break
                        }
                    }
                    /*val array = arrayOfNulls<LocalTime>(time.size)
                    time.toArray(array)*/
                    list_time.adapter = ArrayAdapter(activity!!.applicationContext, R.layout.time_list, time)
                }
            }
        }
    }

    companion object {
        private val ARG_PARAM = "DocId"

        fun newInstance(str: String): TimeFragment {
            val fragment = TimeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM, str)
            fragment.arguments = args
            return fragment
        }
    }

}



