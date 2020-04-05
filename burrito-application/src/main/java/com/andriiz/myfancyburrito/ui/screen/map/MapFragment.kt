package com.andriiz.myfancyburrito.ui.screen.map

import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
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

    private val args by args<MapFragmentArgs>()

    private val viewModel: MapViewModel by fragmentViewModel()

    private var mapView: MapView? = null

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, vg: ViewGroup?, bundle: Bundle?): View =
        inflater.inflate(R.layout.fragment_map, vg, false).apply {
            mapView = findViewById(R.id.mapView)
            mapView?.onCreate(bundle)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView?.getMapAsync {
            googleMap = it
            with(it.uiSettings) {
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = false
            }
        }

        sharedElement.transitionName = args.id

        mapView?.animate()
            ?.setStartDelay(GOOD_DURATION_TIME)
            ?.alpha(FULL_ALPHA)
            ?.start()

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
        state.business.fold(
            ifEmpty = { /* (may be some other day) */ },
            ifSome  = { business ->
                val latLng = LatLng(business.lat, business.lng)

                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, BEST_ZOOM_VALUE))
                googleMap?.addMarker(MarkerOptions().apply { position(latLng) })

                setToolbarTitle(business.name)
                businessAddress.text = business.address
                businessInfo.text = business.info
            })
        return@withState
    }

    companion object {

        private const val BEST_ZOOM_VALUE = 16F

        private const val GOOD_DURATION_TIME = 200L
        private const val FULL_ALPHA = 1F

        fun instance(id: String): Fragment {
            val fragment = MapFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, MapFragmentArgs(id))
            }
            return fragment
        }

    }

}