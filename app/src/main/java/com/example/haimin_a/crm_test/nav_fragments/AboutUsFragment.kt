package com.example.haimin_a.crm_test.nav_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.haimin_a.crm_test.R
import com.squareup.picasso.Picasso

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        Picasso.get().load(getString(R.string.uri_about)).into(view.findViewById<ImageView>(R.id.about_img))
        return view
    }


}


