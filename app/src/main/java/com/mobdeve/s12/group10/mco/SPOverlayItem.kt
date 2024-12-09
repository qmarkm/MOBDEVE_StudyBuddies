package com.mobdeve.s12.group10.mco

import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.util.GeoPoint
import com.google.firebase.firestore.GeoPoint as GeoPointFS

class SPOverlayItem(sp: StudyPact) : OverlayItem(sp.name, sp.description, GeoPoint(sp.location.latitude, sp.location.longitude)) {
    val studyPact = sp
}