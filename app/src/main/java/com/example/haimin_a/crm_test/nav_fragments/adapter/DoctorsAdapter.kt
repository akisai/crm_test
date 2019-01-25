package com.example.haimin_a.crm_test.nav_fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.rest_client.DoctorsInfo

class DoctorsAdapter(private val context: Context, private val data: ArrayList<DoctorsInfo>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.list_item_doctor, parent, false)
        val title = view.findViewById(R.id.title) as TextView
        val subtitle = view.findViewById(R.id.subtitle) as TextView
        val starttime = view.findViewById(R.id.starttime) as TextView
        val endtime = view.findViewById(R.id.endtime) as TextView

        val doctor = getItem(position) as DoctorsInfo

        title.text = doctor.name + " " + doctor.surname
        subtitle.text = doctor.birthday
        starttime.text = doctor.start
        endtime.text = doctor.end

        return view
    }


    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = data.size


}