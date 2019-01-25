package com.example.haimin_a.crm_test.nav_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.nav_fragments.adapter.DoctorsAdapter
import com.example.haimin_a.crm_test.rest_client.DoctorsInfo
import com.example.haimin_a.crm_test.rest_client.Operations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_doctor.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread
import java.net.URL

class DoctorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)
        setList()
        return view
    }

    fun setList() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get doctors info...")
        dialogLog.setCancelable(false)
        doAsync {
            val response = URL(getString(R.string.rest_url) + Operations.getDoctors.str).readText()
            uiThread {
                dialogLog.dismiss()
                if (response.isNotEmpty()) {
                    val gson: ArrayList<DoctorsInfo> =
                        Gson().fromJson(response, object : TypeToken<List<DoctorsInfo>>() {}.type)
                    doctors_list.adapter = DoctorsAdapter(activity!!, gson)
                    doctors_list.setOnItemClickListener { _, _, _, _ ->
                        activity!!.supportFragmentManager.beginTransaction().detach(this@DoctorFragment).attach(TaskFragment()).commit()
                    }
                }
            }
        }
    }

}
