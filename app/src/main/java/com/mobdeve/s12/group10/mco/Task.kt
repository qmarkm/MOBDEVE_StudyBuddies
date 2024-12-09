package com.mobdeve.s12.group10.mco

data class Task(var id: Long, var name: String, var date: String, var time: String, var desc: String) {

    companion object {
        private const val DEFAULT_ID = -1L
        private const val DEFAULT_NAME = "Untitled Task"
        private const val DEFAULT_DATE = "2024-12-01"
        private const val DEFAULT_TIME = "10:00"
        private const val DEFAULT_DESC = "No Description"
    }

    constructor(name: String, date: String, time: String, desc: String) : this(DEFAULT_ID, name, date, time, desc)
    constructor() : this(DEFAULT_ID, DEFAULT_NAME, DEFAULT_DATE, DEFAULT_TIME, DEFAULT_DESC)

    inner class TaskInitializationException(message: String) : Exception(message)
}
