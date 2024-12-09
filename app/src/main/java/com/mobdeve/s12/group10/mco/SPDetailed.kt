package com.mobdeve.s12.group10.mco

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint as GeoPointFS
import com.mobdeve.s12.group10.mco.databinding.ActivitySpdetailedBinding
import com.mobdeve.s12.group10.mco.databinding.DialogSpupdateBinding
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Credits to: https://www.youtube.com/watch?v=TMQNvnU0_wE
 *      For Dialog popup
 */

class SPDetailed : AppCompatActivity(), OnDatePass, OnTimePass {
    private lateinit var viewBinding: ActivitySpdetailedBinding
    private lateinit var spTitle: String
    private lateinit var spDesc: String
    private lateinit var spDateTime: String
    private lateinit var spLocation: GeoPointFS
    private lateinit var spId: String
    private lateinit var spStatus: String
    private var updateDialog: Dialog? = null

    //OSM Map Attributes
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView
    private lateinit var uMap : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpdetailedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        spId = intent.getStringExtra("SP_ID").toString()
        spTitle = intent.getStringExtra("SP_TITLE").toString()
        spDesc = intent.getStringExtra("SP_DESCRIPTION").toString()
        spDateTime = intent.getStringExtra("SP_DATETIME").toString()
        spLocation = GeoPointFS(intent.getDoubleExtra("SP_LATITUDE", 4.5648), intent.getDoubleExtra("SP_LONGITUDE", 120.9932))
        spStatus = intent.getStringExtra("SP_STATUS").toString()

        viewBinding.txvTitle.text = spTitle
        viewBinding.txvDesc.text = spDesc
        viewBinding.txvDateField.text = formatDate(spDateTime)
        viewBinding.txvTimeField.text = formatTime(spDateTime)
        viewBinding.txvStatus.text = "Status: " + spStatus

        viewBinding.btnReturn.setOnClickListener {
            finish()
        }

        viewBinding.btnCreate.setOnClickListener {
            showUpdateDialog(this)
        }

        viewBinding.btnStudyPact.setOnClickListener {
            val intent = Intent(this, MainStudyPacts::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        viewBinding.btnAccount.setOnClickListener {
            val intent = Intent(this, AccountSettings::class.java)
            this.startActivity(intent)
        }

        viewBinding.btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        viewBinding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.startActivity(intent)
        }

        if (spStatus == "Cancelled" || spStatus == "Finished") {
            viewBinding.btnJoin.visibility = View.GONE
            viewBinding.btnCreate.visibility = View.GONE
        }

        viewBinding.btnJoin.setOnClickListener {
            FirebaseApp.initializeApp(this)
            val db = FirebaseFirestore.getInstance()

            db.collection("studyPacts").document(spId).get().addOnSuccessListener { result ->
                val joinedUsers = ArrayList(result.get("joiningUsers") as? List<String>)
                val wasJoined : Boolean
                if (getLoggedInUserEmail() in joinedUsers){
                    wasJoined = true
                    joinedUsers.remove(getLoggedInUserEmail())
                    viewBinding.btnJoin.text = "Join"
                    viewBinding.btnJoin.setTextColor(ContextCompat.getColor(this, R.color.babyGreen))
                    viewBinding.btnJoin.setBackgroundResource(R.drawable.button_gradient2)
                } else {
                    wasJoined = false
                    joinedUsers.add(getLoggedInUserEmail())
                    viewBinding.btnJoin.text = "Joined"
                    viewBinding.btnJoin.setTextColor(ContextCompat.getColor(this, R.color.swampyGreen))
                    viewBinding.btnJoin.setBackgroundResource(R.drawable.button4)
                }

                val sp = hashMapOf(
                    "joiningUsers" to joinedUsers
                )

                db.collection("studyPacts").document(spId).update(sp as Map<String, Any>).addOnSuccessListener {
                    if (wasJoined)
                        Toast.makeText(this, "Left \"${spTitle}\"", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this, "Joined \"${spTitle}\" ", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Map API stuff
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        map = viewBinding.osmmap
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(19)
        val startPoint = GeoPoint(spLocation.latitude, spLocation.longitude)        //lozol
        mapController.setCenter(startPoint);

        //Zoom for Map
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem(spTitle, spDesc, GeoPoint(spLocation.latitude, spLocation.longitude)))

        var overlay = ItemizedOverlayWithFocus<OverlayItem>(items, object: ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index:Int, item: OverlayItem):Boolean {
                return true
            }
            override fun onItemLongPress(index:Int, item: OverlayItem):Boolean {
                return false
            }
        }, this)
        overlay.setFocusItemsOnTap(true)
        map.overlays.add(overlay)
    }

    fun formatDate(date: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(date))
    }

    fun formatTime(time: String): String{
        val dbFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val readableFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        return readableFormat.format(dbFormat.parse(time))
    }

    fun showUpdateDialog(activity: AppCompatActivity) {
        val dialogViewBinding = DialogSpupdateBinding.inflate(layoutInflater)

        updateDialog = Dialog(this, R.style.DialogStyle)
        updateDialog!!.setContentView(dialogViewBinding.root)

        updateDialog!!.getWindow()?.setBackgroundDrawableResource(R.drawable.button4)

        val btnClose = dialogViewBinding.btnClose
        btnClose.setOnClickListener {
            updateDialog!!.dismiss()
        }

        dialogViewBinding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to permanently cancel ${spTitle}?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    FirebaseApp.initializeApp(this)
                    val db = FirebaseFirestore.getInstance()

                    val cancel = hashMapOf(
                        "status" to "Cancelled"
                    )

                    db.collection("studyPacts").document(spId).update(cancel as Map<String, Any>).addOnSuccessListener {
                        Toast.makeText(this, "Study Pact \"$spTitle\" cancelled.", Toast.LENGTH_SHORT).show()
                    }

                    updateDialog!!.dismiss()
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        dialogViewBinding.lytPickDate.setOnClickListener {
            val newFragment = DatePickerFragment(updateDialog!!)
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }

        dialogViewBinding.lytPickTime.setOnClickListener {
            val newFragment = TimePickerFragment(updateDialog!!)
            newFragment.show(activity.supportFragmentManager, "timePicker")
        }

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        uMap = updateDialog!!.findViewById<MapView>(R.id.osmmapUpdate)
        uMap.setTileSource(TileSourceFactory.MAPNIK)

        val uMapController = uMap.controller
        uMapController.setZoom(19)
        val uStartPoint = GeoPoint(spLocation.latitude, spLocation.longitude)
        uMapController.setCenter(uStartPoint)

        uMap.setBuiltInZoomControls(true)
        uMap.setMultiTouchControls(true)

        val uItems = ArrayList<OverlayItem>()
        uItems.add(OverlayItem(spTitle, spDesc, GeoPoint(spLocation.latitude, spLocation.longitude)))

        var geolocation: GeoPoint? = null
        var previousMarker: Marker? = null

        if (uItems.isNotEmpty()) {
            val initialItem = uItems[0]
            previousMarker = Marker(uMap).apply {
                position = initialItem.point as GeoPoint?
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            uMap.overlays.add(previousMarker)
        }

        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    geolocation = p

                    val marker = Marker(uMap)
                    marker.position = p
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    if (previousMarker != null)
                       uMap.overlays.remove(previousMarker)
                    previousMarker = marker

                    uMap.overlays.add(marker)
                    uMap.invalidate()
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return true
            }
        })
        uMap.overlays.add(tapOverlay)

        val btnUpdate = dialogViewBinding.btnCreate
        btnUpdate.setOnClickListener {
            if (checkInputFields()) {
                val inputFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault())

                FirebaseApp.initializeApp(this)
                val db = FirebaseFirestore.getInstance()

                val dateTime = "${dialogViewBinding.txvDateField.text.toString()} ${dialogViewBinding.txvTimeField.text.toString()}"
                val parsedDate: Date = inputFormat.parse(dateTime)

                spTitle = dialogViewBinding.txvTitleField.text.toString()
                spDesc = dialogViewBinding.txvDescField.text.toString()
                val spDateTimeTs: Timestamp = Timestamp(parsedDate)
                spLocation = GeoPointFS(geolocation!!.latitude, geolocation!!.longitude)

                val studyPact = hashMapOf(
                    "name" to spTitle,
                    "dateTime" to spDateTimeTs,
                    "location" to spLocation,
                    "description" to spDesc,
                )

                db.collection("studyPacts").document(spId).update(studyPact as Map<String, Any>).addOnSuccessListener {
                    Toast.makeText(this, "Study Pact \"$spTitle\" updated.", Toast.LENGTH_SHORT).show()
                }

                updateDialog!!.dismiss()
                finish()
            } else {
                Toast.makeText(this, "Please input every field", Toast.LENGTH_SHORT).show()
            }
        }

        updateDialog!!.findViewById<TextView>(R.id.txvTitleField).text = spTitle
        updateDialog!!.findViewById<TextView>(R.id.txvTitle).text = "Update $spTitle"
        updateDialog!!.findViewById<TextView>(R.id.txvDateField).text = formatDate(spDateTime)
        updateDialog!!.findViewById<TextView>(R.id.txvTimeField).text = formatTime(spDateTime)
        updateDialog!!.findViewById<TextView>(R.id.txvDescField).text = spDesc

        updateDialog!!.show()
    }


    override fun onDatePass(data: String) {
        updateDialog!!.findViewById<TextView>(R.id.txvDateField)?.text = data
    }

    override fun onTimePass(data: String) {
        updateDialog!!.findViewById<TextView>(R.id.txvTimeField)?.text = data
    }

    fun checkInputFields() : Boolean {
        if (updateDialog!!.findViewById<TextView>(R.id.txvTitleField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvDateField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvTimeField).text.isNullOrBlank())
            return false

        if (updateDialog!!.findViewById<TextView>(R.id.txvDescField).text.isNullOrBlank())
            return false

        return true
    }

    fun getLoggedInUserEmail(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUserEmail", null)
    }
}