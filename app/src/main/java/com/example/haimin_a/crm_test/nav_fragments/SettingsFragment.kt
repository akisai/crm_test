package com.example.haimin_a.crm_test.nav_fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.haimin_a.crm_test.R


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.findViewById<Button>(R.id.delete_rasp).setOnClickListener {
            deleteRasp()
        }
        setFields()
        return view
    }

    private fun setFields() {

    }

    private fun deleteRasp() {

    }
}
