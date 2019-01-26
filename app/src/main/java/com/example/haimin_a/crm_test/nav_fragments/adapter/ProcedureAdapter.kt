package com.example.haimin_a.crm_test.nav_fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.rest_client.ProcedureInfo

class ProcedureAdapter(private val context: Context, private val data: ArrayList<ProcedureInfo>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.list_item_procedure, parent, false)
        val title = view.findViewById(R.id.titlep) as TextView
        val subtitle = view.findViewById(R.id.subtitlep) as TextView
        val cost = view.findViewById(R.id.costp) as TextView

        val procedure = getItem(position) as ProcedureInfo

        title.text = procedure.name
        subtitle.text = procedure.description
        cost.text = procedure.cost.toString().plus(" \u20BD")

        return view
    }

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = data.size
}