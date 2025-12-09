package com.example.coffeehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider

class MapFragment : Fragment() {
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapview)

        val placemark = mapView.map.mapObjects.addPlacemark().apply{
            geometry = Point(52.951203, 36.063539)
            setIcon(ImageProvider.fromResource(requireContext(), R.drawable.ic_pin_png))
        }
        placemark.setIconStyle(
            IconStyle().apply {
                scale = 0.1f
            }
        )
        placemark.isDraggable = false

        val cameraPosition = CameraPosition(
            Point(52.970747, 36.064192),
            18.0f,
            0.0f,
            0.0f
        )

        mapView.map.move(
            cameraPosition, Animation(Animation.Type.SMOOTH, 2f),
            null
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}