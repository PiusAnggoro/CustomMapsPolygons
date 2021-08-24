package com.piusanggoro.custommaps

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPolygonClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class PolyActivity : AppCompatActivity(), OnMapReadyCallback, OnPolylineClickListener,
    OnMarkerClickListener, OnPolygonClickListener {
    private val WSC = LatLng(-31.952854, 115.857342)
    private val PantaiSowa = LatLng(-33.87365, 151.20689)

    private lateinit var markerWSC: Marker
    private lateinit var markerPantaiSowa: Marker

    private val COLOR_BLACK_ARGB = -0x1000000
    private val POLYLINE_STROKE_WIDTH_PX = 6

    private val PATTERN_GAP_LENGTH_PX = 12
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_GREEN_ARGB = -0xc771c4
    private val COLOR_ORANGE_ARGB = -0xa80e9
    private val COLOR_BLUE_ARGB = -0x657db
    private val POLYGON_STROKE_WIDTH_PX = 6
    private val PATTERN_DASH_LENGTH_PX = 12
    private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val PATTERN_POLYGON_ALPHA = listOf(GAP, DASH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        markerWSC = googleMap.addMarker(
            MarkerOptions()
                .position(WSC)
                .title("Whale Shark Center")
        )
        markerWSC.tag = 0

        markerPantaiSowa = googleMap.addMarker(
            MarkerOptions()
                .position(PantaiSowa)
                .title("Pantai Sowa")
        )
        markerPantaiSowa.tag = 0

        val polyline1 = googleMap.addPolyline(PolylineOptions()
            .clickable(true)
            .add(
                LatLng(-3.3392728661499556, 135.0157287813812),
                LatLng(-3.332630230010517, 135.01666592195463),
                LatLng(-3.3255666702989854, 135.01558818244555),
                LatLng(-3.321777610489474, 135.01488532067887),
                LatLng(-3.318783770951058, 135.01568189247732),
                LatLng(-3.315789913566868, 135.01474474859282),
                LatLng(-3.3134509415816398, 135.01357331915574),
                LatLng(-3.312328216831115, 135.01127731455003)))
        polyline1.tag = "Boat"
        stylePolyline(polyline1)

        val polygon1 = googleMap.addPolygon(PolygonOptions()
            .clickable(true)
            .add(
                LatLng(-3.3067111753473215, 135.00063197431388),
                LatLng(-3.3065187211105536, 135.02438221962657),
                LatLng(-3.3234961247377455, 135.0247064995349),
                LatLng(-3.325275633561162, 135.00047700154588)))
        polygon1.tag = "Penangkaran"
        stylePolygon(polygon1)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-3.3269758,135.0119955), 14f))
        googleMap.setOnPolylineClickListener(this)
        googleMap.setOnPolygonClickListener(this)
        googleMap.setOnMarkerClickListener(this)
    }

    /** Styles the polyline, based on type. */
    private fun stylePolyline(polyline: Polyline) {
        polyline.startCap = RoundCap()
        polyline.endCap = CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 5f)
        polyline.jointType = JointType.ROUND
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = COLOR_BLACK_ARGB
    }

    /** Listens for clicks on a polyline. */
    override fun onPolylineClick(polyline: Polyline) {
        if (polyline.pattern == null || !polyline.pattern!!.contains(DOT)) {
            polyline.pattern = PATTERN_POLYLINE_DOTTED
        } else {
            polyline.pattern = null
        }
        Toast.makeText(this, "Rute " + polyline.tag.toString(), Toast.LENGTH_SHORT).show()
    }

    /** Styles the polygon, based on type. */
    private fun stylePolygon(polygon: Polygon) {
        var pattern: List<PatternItem>? = PATTERN_POLYGON_ALPHA

        polygon.strokePattern = pattern
        polygon.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
        polygon.strokeColor = COLOR_BLACK_ARGB
        polygon.fillColor = COLOR_WHITE_ARGB
    }

    /** Listens for clicks on a polygon. */
    override fun onPolygonClick(polygon: Polygon) {
        polygon.strokeColor = COLOR_ORANGE_ARGB
        polygon.fillColor = COLOR_GREEN_ARGB
        Toast.makeText(this, "Area ${polygon.tag?.toString()}", Toast.LENGTH_SHORT).show()
    }

    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(this, "Lokasi ${marker.title}", Toast.LENGTH_SHORT).show()
        return false
    }
}
