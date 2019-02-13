package com.example.haimin_a.crm_test.nav_fragments


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import kotlinx.android.synthetic.main.fragment_record_media.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class RecordMediaFragment : Fragment() {

    private val VIDEO_CAPTURE = 101
    private val IMAGE_CAPTURE = 202
    private val SOUND_CAPTURE = 303
    lateinit var mediaRecorder: MediaRecorder
    private var state = false
    private var recordingStopped = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record_media, container, false)
    }

    override fun onStart() {
        super.onStart()
        video.isEnabled = hasCamera()
        photo.isEnabled = hasCamera()

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        }
        initMediaRecorder()
        start.setOnClickListener {
            startRecordingSound()
        }

        pause.setOnClickListener {
            pauseRecordingSound()
        }

        stop.setOnClickListener {
            stopRecordingSound()
        }

        video.setOnClickListener {
            startRecordingVideo()
        }

        photo.setOnClickListener {
            startRecordImage()
        }
    }


    private fun initMediaRecorder() {
        val output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"

        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(output)
    }

    private fun startRecordingVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_CAPTURE)
    }

    private fun startRecordImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_CAPTURE)
    }

    private fun startRecordingSound() {
        mediaRecorder.prepare()
        mediaRecorder.start()
        state = true
        context?.toast("Record started")
    }

    private fun stopRecordingSound() {
        if (state) {
            mediaRecorder.stop()
            mediaRecorder.release()
            state = false
        } else {
            context?.toast("You are not recording right now!")
        }
    }

    private fun pauseRecordingSound() {
        if (state) {
            if (!recordingStopped) {
                context?.toast("Stopped!")
                mediaRecorder.pause()
                recordingStopped = true
                pause.text = "Resume"
            } else {
                resumeRecordingSound()
            }
        }
    }

    private fun resumeRecordingSound() {
        context?.toast("Resume!")
        mediaRecorder.resume()
        pause.text = "Pause"
        recordingStopped = false
    }

    private fun hasCamera() = activity!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent
    ) {
        val videoUri = data.data
        if (requestCode == VIDEO_CAPTURE) {
            when (resultCode) {
                Activity.RESULT_OK -> context?.longToast("Video saved to:\n" + videoUri)
                Activity.RESULT_CANCELED -> context?.longToast("Video recording cancelled.")
                else -> context?.longToast("Failed to record video")
            }
        }
    }


}
