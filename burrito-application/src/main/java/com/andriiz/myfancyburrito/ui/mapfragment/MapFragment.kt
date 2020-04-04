package com.andriiz.myfancyburrito.ui.mapfragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.andriiz.domain.data.info
import com.andriiz.myfancyburrito.R
import com.andriiz.myfancyburrito.ui.core.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_map.*


@Parcelize
data class MapFragmentArgs(val id: String) : Parcelable

class MapFragment : BaseFragment() {

    private val viewModel: MapViewModel by fragmentViewModel()

    private var mapView: MapView? = null

    private var googleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, vg: ViewGroup?, bundle: Bundle?): View =
        inflater.inflate(R.layout.fragment_map, vg, false).apply {
            mapView = findViewById(R.id.mapView)
            mapView?.onCreate(bundle)
            mapView?.getMapAsync {
                googleMap = it
                with(it.uiSettings){
                    isScrollGesturesEnabled = false
                    isZoomGesturesEnabled = false
                }
            }
        }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        showBackButton(true)
    }

    override fun onStop() {
        mapView?.onPause()
        showBackButton(false)
        super.onStop()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onBackPressed(): Boolean = false

    override fun invalidate() = withState(viewModel) { state ->
        state.business?.let { business ->

            val latLng = LatLng(business.lat, business.lng)

            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, BEST_ZOOM_VALUE))
            googleMap?.addMarker(MarkerOptions().apply { position(latLng) })

            setToolbarTitle(business.name)
            businessTitle.text = business.address
            businessInfo.text = business.info

        }
        return@withState
    }

    companion object {

        private const val BEST_ZOOM_VALUE = 16F

        fun instance(id: String) : Fragment {
            val fragment = MapFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, MapFragmentArgs(id))
            }
            return fragment
        }
    }

}