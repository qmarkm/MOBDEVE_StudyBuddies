package com.mobdeve.s12.group10.mco

import org.json.JSONObject
import java.util.Calendar

//TODO: Finish class, temporarily made for RecyclerView parameters


data class StudyPact (
    val id : Int,
    val name: String,
    val creator: Int,
    val dateTime: String,
    val location: String,
    val description: String,
    val joiningUsers: String
)