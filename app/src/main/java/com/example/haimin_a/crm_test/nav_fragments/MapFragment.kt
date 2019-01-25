package com.example.haimin_a.crm_test.nav_fragments


import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.haimin_a.crm_test.R
import com.example.haimin_a.crm_test.utils.getPoints
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!.applicationContext)
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        val mainOffice = LatLng(59.8242617, 30.5128346)
        mMap.addMarker(MarkerOptions().position(mainOffice).title("Main office"))
        mainOfficeB.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mainOffice, 12f))
        }
        mainOfficeB.show()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainOffice, 12.0f))

        setUpMap()
    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(activity!!) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                /*doAsync {
                    val str = URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + currentLatLng + "&destination=" + mainOffice + "&key=AIzaSyDeW26ZL27BfXqhxpH_D7p7kLTTdRUSN6w").readText()
                    val steps = JSONObject(str).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps")
                    uiThread {
                        for (i in 0 until steps.length()) {
                            val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                            path.add(PolyUtil.decode(points))
                        }
                        for (i in 0 until path.size) {
                            mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.BLUE))
                        }
                    }
                }*/
            }
        }
        val path = getPoints()
        for (i in 0 until path.size)
            mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.BLUE))
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}

