package com.mobdeve.s12.group10.mco

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivityMainStudyPactsBinding
import java.text.SimpleDateFormat
import java.util.Locale

import android.Manifest
import android.content.pm.PackageManager
import android.preference.PreferenceManager

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem

import java.util.ArrayList
import com.google.firebase.firestore.GeoPoint as GeoPointFS
/**
 * Credits to:
 * → https://www.youtube.com/watch?app=desktop&v=ROkKPgXpd1Y
 * → https://github.com/android/platform-samples/blob/main/samples/user-interface/constraintlayout/src/main/res/xml/scene_02.xml
 * → and Mr. Berris
 *
 * For the Motion Layout in activity_main_study_pacts.xml & activity_main_study_pacts_scene.xml
 */

class MainStudyPacts : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainStudyPactsBinding
    private val spList: SPList = SPList(this)
    private lateinit var spArray: ArrayList<StudyPact>

    //OSM Map Attributes
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainStudyPactsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        spArray = spList.getLoggedInSP()

        viewBinding.rcvStudyPacts.adapter = SPAdapter(spArray, this)
        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)

        viewBinding.btnViewSP.setOnClickListener {
            val intent = Intent(this, SPView::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnSelfSP.setOnClickListener {
            val intent = Intent(this, SPSelf::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }

        //Map API stuff
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        map = viewBinding.osmmap
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(19)
        val startPoint = GeoPoint(14.5648, 120.9932)        //lozol
        mapController.setCenter(startPoint);

        //Zoom for Map
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        Log.d("TAG", "MAPPE")

        //your items
        val items = ArrayList<SPOverlayItem>()
        for (sp in spArray) {
            items.add(SPOverlayItem(sp))
            Log.d("TAG", sp.name)
        }

        for (item in items) {
            val marker = Marker(map)
            marker.position = item.point as GeoPoint?
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            map.overlays.add(marker)
            Log.d("TAG", item.studyPact.name)
        }

        //the overlay
        var overlay = ItemizedOverlayWithFocus<OverlayItem>(items as List<OverlayItem>?, object: ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index:Int, item:OverlayItem):Boolean {
                val spItem = item as SPOverlayItem
                val intent = Intent(this@MainStudyPacts, SPDetailed::class.java)

                intent.putExtra("SP_ID", spItem.studyPact._id)
                intent.putExtra("SP_TITLE", spItem.studyPact.name)
                intent.putExtra("SP_DATETIME", spItem.studyPact.dateTime)
                intent.putExtra("SP_LOCATION", "There")
                intent.putExtra("SP_DESCRIPTION", spItem.studyPact.description)
                intent.putExtra("SP_USERS", spItem.studyPact.joiningUsers)
                intent.putExtra("SP_STATUS", spItem.studyPact.status)

                this@MainStudyPacts.startActivity(intent)
                return true
            }
            override fun onItemLongPress(index:Int, item:OverlayItem):Boolean {
                return false
            }
        }, this)
        overlay.setFocusItemsOnTap(true)

        map.overlays.add(overlay)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }
}