package com.example.haimin_a.crm_test.nav_fragments.core

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.nav_fragments.adapter.DoctorsAdapter
import com.example.haimin_a.crm_test.rest_client.DoctorsInfo
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.utils.getGetResponse
import com.example.haimin_a.crm_test.utils.processingResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_doctor.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread

class DoctorFragment : Fragment() {

    private var procedure: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            procedure = arguments!!.getString(ARG_PARAM)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor, container, false)
        setList()
        return view
    }

    private fun setList() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get doctors info...")
        dialogLog.setCancelable(false)
        doAsync {
            val response = getGetResponse(getString(R.string.rest_url) + Operations.getDoctors.str)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(activity!!, response, needSecond = false)) {
                    val gson: ArrayList<DoctorsInfo> =
                        Gson().fromJson(response, object : TypeToken<List<DoctorsInfo>>() {}.type)
                    if (procedure != null) {
                        val list: ArrayList<DoctorsInfo> = ArrayList()
                        for (d in gson) {
                            if (procedure!!.contains(d.id.toString())) {
                                list.add(d)
                            }
                        }
                        doctors_list.adapter = DoctorsAdapter(activity!!, list)
                        doctors_list.setOnItemClickListener { _, _, position, _ ->
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.navigation_content,
                                    TimeFragment.newInstance(gson[position].id.toString()),
                                    "Choose Time"
                                )
                                .addToBackStack(null).commit()
                        }
                    } else {
                        doctors_list.adapter = DoctorsAdapter(activity!!, gson)
                        doctors_list.setOnItemClickListener { _, _, position, _ ->
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.navigation_content,
                                    TaskFragment.newInstance(gson[position].procedure),
                                    "Choose Procedure"
                                )
                                .addToBackStack(null).commit()
                        }
                    }

                }
            }
        }
    }

    companion object {
        private val ARG_PARAM = "Procedure"

        fun newInstance(str: String): DoctorFragment {
            val fragment = DoctorFragment()
            val args = Bundle()
            args.putString(ARG_PARAM, str)
            fragment.arguments = args
            return fragment
        }
    }
}
