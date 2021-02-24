package com.example.miooapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.*
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import androidx.fragment.app.Fragment
import com.example.miooapp.R
import com.mapbox.api.geocoding.v5.models.CarmenFeature
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceAutocompleteFragment.TAG
import com.mapbox.mapboxsdk.plugins.places.autocomplete.ui.PlaceSelectionListener
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions
import kotlinx.android.synthetic.main.activity_map.view.*


class MapFragment : Fragment(),
    OnMapReadyCallback, OnLocationClickListener, PermissionsListener,
    OnCameraTrackingChangedListener {

    private var permissionsManager: PermissionsManager? = null
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var locationComponent: LocationComponent? = null
    private var isInTrackingMode = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireActivity(), getString(R.string.mapbox_access_token))

        val view = inflater.inflate(R.layout.activity_map, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        val autocompleteFragment: PlaceAutocompleteFragment

        if (savedInstanceState == null) {

            autocompleteFragment =
                PlaceAutocompleteFragment.newInstance("dnRkcjFyMnN")
            val transaction = requireFragmentManager().beginTransaction()
            transaction.add(R.id.fragment, autocompleteFragment, TAG)
            transaction.commit()

        } else {
            autocompleteFragment =
                requireFragmentManager().findFragmentByTag(TAG) as PlaceAutocompleteFragment
        }
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(carmenFeature: CarmenFeature) {
                Toast.makeText(
                    context,
                    carmenFeature.text(), Toast.LENGTH_LONG
                ).show()

            }

            override fun onCancel() {

                requireView().mapView!!.onStop()
            }
        })

        PlaceAutocomplete.clearRecentHistory(context);
        val intent = PlacePicker.IntentBuilder()
            .accessToken(Mapbox.getAccessToken()!!)
            .placeOptions(
                PlacePickerOptions.builder()
                    .statingCameraPosition(
                        CameraPosition.Builder()
                            .target(LatLng(10.8970, 8.3422))
                            .zoom(16.0)
                            .build()
                    )
                    .build()
            )
            .build(activity)
        startActivityForResult(intent, 20)




return view
    }
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
   { super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 20 && resultCode == Activity.RESULT_OK) {

            val carmenFeature = PlacePicker.getPlace(data)

        }
    }
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        val options = MarkerOptions()
        options.title("curent positions ")
        options.position(LatLng(10.8970, 8.3422))
        mapboxMap.addMarker(options)
        mapboxMap.setStyle(
            Style.LIGHT
        ) { style -> enableLocationComponent(style) }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {

        if (PermissionsManager.areLocationPermissionsGranted(activity)) {


            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .elevation(5f)
                .accuracyAlpha(.6f)
                .accuracyColor(Color.RED)
                .foregroundDrawable(R.drawable.ic_baseline_location_on_24)
                .build()


            locationComponent = mapboxMap!!.locationComponent
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireView().context, loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            locationComponent!!.activateLocationComponent(locationComponentActivationOptions)

            locationComponent!!.isLocationComponentEnabled = true

            locationComponent!!.cameraMode = CameraMode.TRACKING

            locationComponent!!.renderMode = RenderMode.COMPASS

            locationComponent!!.addOnLocationClickListener(this)


            locationComponent!!.addOnCameraTrackingChangedListener(this)
            requireView().findViewById<View>(R.id.back_to_camera_tracking_mode).setOnClickListener {
                if (!isInTrackingMode) {
                    isInTrackingMode = true
                    locationComponent!!.cameraMode =
                        CameraMode.TRACKING
                    locationComponent!!.zoomWhileTracking(16.0)
                    Toast.makeText(
                        requireView().context, getString(R.string.tracking_enabled),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.tracking_already_enabled),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(activity)
        }
    }

    override fun onLocationComponentClick() {
        if (locationComponent!!.lastKnownLocation != null) {
            Toast.makeText(
                requireContext(), String.format(
                    getString(R.string.current_location),
                    locationComponent!!.lastKnownLocation!!.latitude,
                    locationComponent!!.lastKnownLocation!!.longitude
                ), Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCameraTrackingDismissed() {
        isInTrackingMode = false
    }

    override fun onCameraTrackingChanged(currentMode: Int) {
        // Empty on purpose
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(
            requireContext(),
            R.string.user_location_permission_explanation,
            Toast.LENGTH_LONG
        )
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap!!.getStyle { style -> enableLocationComponent(style) }
        } else {
            Toast.makeText(
                requireContext(),
                R.string.user_location_permission_not_granted,
                Toast.LENGTH_LONG
            )
                .show()
            requireActivity().finish()
        }
    }


    override fun onStart() {
        super.onStart()
        requireView().mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        requireView().mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        requireView().mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        requireView().mapView!!.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       requireView(). mapView!!.onSaveInstanceState(outState)
    }



    override fun onLowMemory() {
        super.onLowMemory()
        requireView().mapView!!.onLowMemory()
    }
}
