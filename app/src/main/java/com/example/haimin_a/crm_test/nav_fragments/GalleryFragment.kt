package com.example.haimin_a.crm_test.nav_fragments


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onStart() {
        super.onStart()

        tabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                when(p0?.text) {
                    "Photo" -> activity!!.supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.navigation_content,
                            ProfileFragment()
                        ).commit()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }
        })
//        val intent = Intent(Intent.ACTION_PICK)
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.type = "video/*"
//        startActivityForResult(intent, 11)
    }
}
