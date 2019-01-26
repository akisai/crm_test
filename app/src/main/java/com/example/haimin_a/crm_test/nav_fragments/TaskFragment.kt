package com.example.haimin_a.crm_test.nav_fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.nav_fragments.adapter.ProcedureAdapter
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.ProcedureInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_task.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread
import java.net.URL


class TaskFragment : Fragment() {

    private var doc: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            doc = arguments!!.getString(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        setList()
        return view
    }

    fun setList() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get procedure info...")
        dialogLog.setCancelable(false)
        doAsync {
            val response = URL(getString(R.string.rest_url) + Operations.getProcedure.str).readText()
            uiThread {
                dialogLog.dismiss()
                if (response.isNotEmpty()) {
                    val gson: ArrayList<ProcedureInfo> =
                        Gson().fromJson(response, object : TypeToken<List<ProcedureInfo>>() {}.type)
                    if (doc != null) {
                        val list: ArrayList<ProcedureInfo> = ArrayList()
                        for (p in gson) {
                                if (doc!!.contains(p.id.toString())) {
                                    list.add(p)
                                }
                        }
                        println(list.size)
                        procedure_list.adapter = ProcedureAdapter(activity!!, list)
                        procedure_list.setOnItemClickListener { _, _, _, _ ->
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.navigation_content,
                                    TimeFragment.newInstance(doc!!),
                                    "Choose Time"
                                )
                                .addToBackStack(null).commit()
                        }
                    } else {
                        procedure_list.adapter = ProcedureAdapter(activity!!, gson)
                        procedure_list.setOnItemClickListener { _, _, position, _ ->
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.navigation_content,
                                    DoctorFragment.newInstance(gson[position].doctors),
                                    "Choose Doctor"
                                )
                                .addToBackStack(null).commit()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val ARG_PARAM = "Doctors"

        fun newInstance(str: String): TaskFragment {
            val fragment = TaskFragment()
            val args = Bundle()
            args.putString(ARG_PARAM, str)
            fragment.arguments = args
            return fragment
        }
    }
}
