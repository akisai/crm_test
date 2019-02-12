package com.example.haimin_a.crm_test.nav_fragments


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import kotlinx.android.synthetic.main.fragment_record_media.*
import org.jetbrains.anko.longToast

class RecordMediaFragment : Fragment() {

    private val VIDEO_CAPTURE = 101
    private val IMAGE_CAPTURE = 202
    private val SOUND_CAPTURE = 303

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record_media, container, false)
    }

    override fun onStart() {
        super.onStart()
        video.isEnabled = hasCamera()
        video.setOnClickListener {
            startRecordingVideo()
        }

        microphone.setOnClickListener {
            startRecordingSound()
        }
    }

    private fun startRecordingVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)//MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_CAPTURE)
    }

    private fun startRecordImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, IMAGE_CAPTURE)
    }

    private fun startRecordingSound() {
        val intent = Intent (MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        startActivityForResult(intent, SOUND_CAPTURE)
    }

    private fun hasCamera() = activity!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent
    ) {
        println("\n\ntest\n\n")
        val videoUri = data.data
        if (requestCode == VIDEO_CAPTURE) {
            when (resultCode) {
                Activity.RESULT_OK -> context?.longToast("Video saved to:\n")
                Activity.RESULT_CANCELED -> context?.longToast("Video recording cancelled.")
                else -> context?.longToast("Failed to record video")
            }
        }
    }

}
