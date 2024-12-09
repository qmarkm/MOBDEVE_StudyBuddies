package com.mobdeve.s12.group10.mco

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "tasks_database.db"
        const val TABLE_NAME = "tasks"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_DESC = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_STATEMENT = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT,
                $COLUMN_DESC TEXT
            )
        """
        db?.execSQL(CREATE_TABLE_STATEMENT)

        val initialTasks = listOf(
            ContentValues().apply {
                put(COLUMN_NAME, "Sample Task 1")
                put(COLUMN_DATE, "2024-12-01")
                put(COLUMN_TIME, "09:00")
                put(COLUMN_DESC, "This is the first sample task.")
            },
            ContentValues().apply {
                put(COLUMN_NAME, "Sample Task 2")
                put(COLUMN_DATE, "2024-12-02")
                put(COLUMN_TIME, "14:00")
                put(COLUMN_DESC, "This is the second sample task.")
            }
        )

        initialTasks.forEach { task ->
            db?.insert(TABLE_NAME, null, task)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE_STATEMENT)
        onCreate(db)
    }

    fun addTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
            put(COLUMN_DESC, task.desc)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null, // Select all columns
            null, // No WHERE clause
            null, // No WHERE arguments
            null, // No GROUP BY
            null, // No HAVING
            null // No ORDER BY
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC))
                taskList.add(Task(id, name, date, time, desc))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return taskList
    }

    fun deleteTask(taskId: Long) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(taskId.toString()))
        db.close()
    }

    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
            put(COLUMN_DESC, task.desc)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(task.id.toString()))
        db.close()
    }
}
