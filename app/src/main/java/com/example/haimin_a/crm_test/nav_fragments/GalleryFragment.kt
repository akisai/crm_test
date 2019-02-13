package com.example.haimin_a.crm_test.nav_fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R

class GalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onStart() {
        super.onStart()
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "video/*"
//        startActivityForResult(intent, 11)
    }
}
