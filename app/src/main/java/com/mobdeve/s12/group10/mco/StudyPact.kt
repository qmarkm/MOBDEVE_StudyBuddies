package com.mobdeve.s12.group10.mco

import com.google.firebase.firestore.GeoPoint as GeoPointFS
import org.json.JSONObject
import java.io.Serializable
import java.util.Calendar

data class StudyPact(
    val _id: String,
    var name: String,
    val creator: String,
    var dateTime: String,
    var location: GeoPointFS,
    var description: String,
    var joiningUsers: ArrayList<String>,
    val status: String
)