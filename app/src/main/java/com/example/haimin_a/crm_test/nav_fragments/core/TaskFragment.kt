package com.example.haimin_a.crm_test.nav_fragments.core


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.nav_fragments.adapter.ProcedureAdapter
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.ProcedureInfo
import com.example.haimin_a.crm_test.utils.getGetResponse
import com.example.haimin_a.crm_test.utils.processingResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_task.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.uiThread


class TaskFragment : Fragment() {

    private var procedure: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            procedure = arguments!!.getStringArrayList(ARG_PARAM)
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

    private fun setList() {
        val dialogLog = activity!!.indeterminateProgressDialog("Get procedure info...")
        dialogLog.setCancelable(false)
        doAsync {
            val response = getGetResponse(getString(R.string.rest_url) + Operations.getProcedure.str)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(activity!!, response, needSecond = false)) {
                    val gson: ArrayList<ProcedureInfo> =
                        Gson().fromJson(response, object : TypeToken<List<ProcedureInfo>>() {}.type)
                    if (procedure != null) {
                        val list: ArrayList<ProcedureInfo> = ArrayList()
                        for (p in gson) {
                            if (procedure!![0].contains(p.id.toString())) {
                                list.add(p)
                            }
                        }
                        procedure_list.adapter = ProcedureAdapter(activity!!, list)
                        procedure_list.setOnItemClickListener { _, _, position, _ ->
                            val time = ArrayList<String>()
                            time.add(procedure!![1])
                            time.add(list[position].id.toString())
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.navigation_content,
                                    TimeFragment.newInstance(time),
                                    "Choose Time"
                                )
                                .addToBackStack(null).commit()
                        }
                    } else {
                        procedure_list.adapter = ProcedureAdapter(activity!!, gson)
                        procedure_list.setOnItemClickListener { _, _, position, _ ->
                            val list = ArrayList<String>()
                            list.add(gson[position].doctors)
                            list.add(gson[position].id.toString())
                            activity!!.supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.navigation_content,
                                    DoctorFragment.newInstance(list),
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
        private val ARG_PARAM = "Procedure"

        fun newInstance(list: ArrayList<String>): TaskFragment {
            val fragment = TaskFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_PARAM, list)
            fragment.arguments = args
            return fragment
        }
    }
}
