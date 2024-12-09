package com.mobdeve.s12.group10.mco

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s12.group10.mco.databinding.ActivitySpselfBinding
import com.mobdeve.s12.group10.mco.databinding.DialogSpcreateBinding
import com.mobdeve.s12.group10.mco.databinding.DialogSpupdateBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.text.TextWatcher
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import com.google.firebase.firestore.GeoPoint as GeoPointFS

class SPSelf : AppCompatActivity(), OnDatePass, OnTimePass {
    private lateinit var viewBinding: ActivitySpselfBinding
    private lateinit var dialogViewBinding: DialogSpcreateBinding
    private var createDialog: Dialog? = null
    private lateinit var spArray: ArrayList<StudyPact>
    private lateinit var studyPactAdapter: SPAdapter

    //Map Stuff
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpselfBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        spArray = ArrayList()
        studyPactAdapter = SPAdapter(spArray, this)
        viewBinding.rcvStudyPacts.layoutManager = LinearLayoutManager(this)
        viewBinding.rcvStudyPacts.adapter = studyPactAdapter

        fetchSP()

        viewBinding.btnAddSP.setOnClickListener {
            showCreateDialog(this)
        }

        viewBinding.btnReturn.setOnClickListener {
            finish()
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

        viewBinding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                search(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onResume(){
        super.onResume()
        fetchSP()
    }

    private fun search(query: String) {
        val filteredList = ArrayList<StudyPact>()
        for (sp in spArray) {
            if (sp.name.contains(query, true)) {
                filteredList.add(sp)
            }
        }
        studyPactAdapter.filterList(filteredList)
    }

    private fun fetchSP() {
        FirebaseApp.initializeApp(this)
        val db = FirebaseFirestore.getInstance()

        val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())

        db.collection("studyPacts").whereIn("creator", listOf(getLoggedInUserEmail())).get().addOnSuccessListener { result ->
            spArray.clear() // Clear the list before adding new data
            for (document in result) {
                val timestamp: Timestamp = document.getTimestamp("dateTime") ?: Timestamp.now()
                val spdatetime = inputFormat.format(timestamp.toDate())

                val sp = StudyPact(
                    document.id,
                    document.getString("name") ?: "Error",
                    document.getString("creator") ?: "dummy@email.com",
                    spdatetime.toString(),
                    (document.get("location") ?: GeoPointFS(4.5648, 120.9932)) as GeoPointFS,
                    document.getString("description") ?: "Error: No values returned",
                    ArrayList(document.get("joiningUsers") as? List<String>),
                    document.getString("status") ?: "Cancelled"
                )
                spArray.add(sp)
            }

            studyPactAdapter.notifyDataSetChanged()
        }
    }

    fun showCreateDialog(activity: AppCompatActivity) {
        val dialogViewBinding = DialogSpcreateBinding.inflate(layoutInflater)

        createDialog = Dialog(this, R.style.DialogStyle)
        createDialog!!.setContentView(dialogViewBinding.root)

        createDialog!!.window?.setBackgroundDrawableResource(R.drawable.button4)

        dialogViewBinding.btnClose.setOnClickListener {
            createDialog!!.dismiss()
        }

        //Map API stuff
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        map = dialogViewBinding.osmmap
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(19)
        val startPoint = GeoPoint(14.5648, 120.9932)        //DLSU
        mapController.setCenter(startPoint);

        //Zoom for Map
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        var geolocation : GeoPoint? = null
        var previousMarker : Marker? = null
        val tapOverlay = MapEventsOverlay(object: MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                if (p != null) {
                    geolocation = p

                    val marker = Marker(map)
                    marker.position = p
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    if (previousMarker != null)
                        map.overlays.remove(previousMarker)
                    previousMarker = marker

                    map.overlays.add(marker)
                    map.invalidate()
                }
                return true
            }
            override fun longPressHelper(p: GeoPoint?): Boolean {
                return true
            }
        })
        map.overlays.add(tapOverlay)

        dialogViewBinding.btnCreate.setOnClickListener {
            if (checkInputFields(geolocation)) {
                val inputFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault())

                FirebaseApp.initializeApp(this)
                val db = FirebaseFirestore.getInstance()

                val spname = dialogViewBinding.txvTitleField.text.toString()
                val spcreator: String = getLoggedInUserEmail().toString()
                val spdate = dialogViewBinding.txvDateField.text.toString()
                val sptime = dialogViewBinding.txvTimeField.text.toString()
                val splocation = GeoPointFS(geolocation!!.latitude, geolocation!!.longitude)
                val spdescription = dialogViewBinding.txvDescField.text.toString()

                val dateTime = "$spdate $sptime"
                val parsedDate: Date = inputFormat.parse(dateTime)
                val spdatetime: Timestamp = Timestamp(parsedDate)

                val arrayJoiningUsers = ArrayList<String>()
                arrayJoiningUsers.add(spcreator)

                val studyPact = hashMapOf(
                    "name" to spname,
                    "creator" to spcreator,
                    "dateTime" to spdatetime,
                    "location" to splocation,
                    "description" to spdescription,
                    "joiningUsers" to arrayJoiningUsers,
                    "status" to "Upcoming"
                )

                db.collection("studyPacts").add(studyPact).addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Study Pact \"$spname\" created.", Toast.LENGTH_SHORT).show()
                }

                createDialog!!.dismiss()
                fetchSP()
            } else {
                Toast.makeText(this, "Please input every field", Toast.LENGTH_SHORT).show()
            }
        }
        dialogViewBinding.txvTitle.text = "Create Study Pact"

        dialogViewBinding.lytPickDate.setOnClickListener {
            val newFragment = DatePickerFragment(createDialog!!)
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }

        dialogViewBinding.lytPickTime.setOnClickListener {
            val newFragment = TimePickerFragment(createDialog!!)
            newFragment.show(activity.supportFragmentManager, "timePicker")
        }

        createDialog!!.show()
    }

    private fun getLoggedInUserEmail(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUserEmail", null)
    }

    override fun onDatePass(data: String) {
        createDialog!!.findViewById<TextView>(R.id.txvDateField)?.text = data
    }

    override fun onTimePass(data: String) {
        createDialog!!.findViewById<TextView>(R.id.txvTimeField)?.text = data
    }

    fun checkInputFields(geolocation : GeoPoint?): Boolean {
        if (createDialog!!.findViewById<TextView>(R.id.txvTitleField).text.isNullOrBlank())
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvDateField).text.isNullOrBlank())
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvTimeField).text.isNullOrBlank())
            return false

        if (geolocation == null)
            return false

        if (createDialog!!.findViewById<TextView>(R.id.txvDescField).text.isNullOrBlank())
            return false

        return true
    }
}
