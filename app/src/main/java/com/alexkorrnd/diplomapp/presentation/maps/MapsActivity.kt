package com.alexkorrnd.diplomapp.presentation.maps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.alexkorrnd.diplomapp.R
import com.alexkorrnd.diplomapp.data.network.responses.AddressesResponse
import com.alexkorrnd.diplomapp.domain.Contact
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback, MapsPresenter.View {

    companion object {

        private val EXTRA_CONTACT = "EXTRA_CONTACT"
        private val EXTRA_ADDRESS = "EXTRA_ADDRESS"

        private val TAG = MapsActivity::class.java.simpleName

        fun createIntent(context: Context, contact: Contact, address: String): Intent {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            intent.putExtra(EXTRA_CONTACT, contact)
            return intent
        }
    }

    private var mMap: GoogleMap? = null

    private var locationResponse: AddressesResponse.AddressesResponse.GeometryResponse.LocationResponse? = null

    private var contact: Contact? = null

    private lateinit var presenter: MapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        presenter = MapsPresenter(this)

        contact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val address = intent.getStringExtra(EXTRA_ADDRESS)

        presenter.loadCoordinates(address)

    }

    override fun onCoordinatesLoaded(locationResponse: AddressesResponse.AddressesResponse.GeometryResponse.LocationResponse) {
        this.locationResponse = locationResponse
        addMarker()
    }

    override fun onShowError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        addMarker()
    }

    private fun addMarker() {
        if (mMap == null || locationResponse == null) {
            return
        }
        val latitude = locationResponse!!.lat
        val longitude = locationResponse!!.lng
        val sydney = LatLng(latitude, longitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title(contact!!.fullName))
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13F))
    }


}
