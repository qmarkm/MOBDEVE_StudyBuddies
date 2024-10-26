package com.mobdeve.s12.group10.mco

import org.json.JSONObject
import java.util.Calendar

//TODO: Finish class, temporarily made for RecyclerView parameters

//TODO: Add ID attribute

data class StudyPact (
    val name: String,
    val dateTime: String,
    val location: String,
    val description: String,
    val joiningUsers: String    //I think this should be JSONObject? It will be an ArrayList of user (IDs), but idk how SQLite will handle Arrays
)