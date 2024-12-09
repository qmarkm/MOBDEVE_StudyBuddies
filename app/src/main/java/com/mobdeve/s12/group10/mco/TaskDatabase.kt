package com.mobdeve.s12.group10.mco

import android.content.ContentValues
import android.content.Context

class TaskDatabase(context: Context) {

    // A private instance of the DB helper
    private var databaseHandler: TaskOpenHelper = TaskOpenHelper(context)

    // Inserts a provided task into the database. Returns the ID of the newly inserted task.
    fun addTask(task: Task): Int {
        val db = databaseHandler.writableDatabase
        val contentValues = ContentValues().apply {
            put(TaskOpenHelper.COLUMN_NAME, task.name)
            put(TaskOpenHelper.COLUMN_DATE, task.date)
            put(TaskOpenHelper.COLUMN_TIME, task.time)
            put(TaskOpenHelper.COLUMN_DESC, task.desc)
        }

        val _id = db.insert(TaskOpenHelper.TABLE_NAME, null, contentValues)
        db.close()
        return _id.toInt()
    }

    // Updates a provided task's details in the database.
    fun updateTask(task: Task) {
        val db = databaseHandler.writableDatabase
        val contentValues = ContentValues().apply {
            put(TaskOpenHelper.COLUMN_NAME, task.name)
            put(TaskOpenHelper.COLUMN_DATE, task.date)
            put(TaskOpenHelper.COLUMN_TIME, task.time)
            put(TaskOpenHelper.COLUMN_DESC, task.desc)
        }

        db.update(
            TaskOpenHelper.TABLE_NAME,
            contentValues,
            "${TaskOpenHelper.COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        )
        db.close()
    }

    // Deletes a specific task from the database by ID.
    fun deleteTask(task: Task) {
        val db = databaseHandler.writableDatabase
        db.delete(
            TaskOpenHelper.TABLE_NAME,
            "${TaskOpenHelper.COLUMN_ID} = ?",
            arrayOf(task.id.toString())
        )
        db.close()
    }

    // Retrieves all tasks from the database and returns them as a list.
    fun getAllTasks(): ArrayList<Task> {
        val result = ArrayList<Task>()
        val db = databaseHandler.readableDatabase
        val query = "SELECT * FROM ${TaskOpenHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_NAME)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_DATE)),
                    time = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_TIME)),
                    desc = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_DESC))
                )
                result.add(task)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return result
    }

    // Retrieves a specific task from the database by ID.
    fun getTaskById(taskId: Long): Task? {
        val db = databaseHandler.readableDatabase
        val query = "SELECT * FROM ${TaskOpenHelper.TABLE_NAME} WHERE ${TaskOpenHelper.COLUMN_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(taskId.toString()))

        var task: Task? = null
        if (cursor.moveToFirst()) {
            task = Task(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_NAME)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_DATE)),
                time = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_TIME)),
                desc = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_DESC))
            )
        }

        cursor.close()
        db.close()
        return task
    }

    fun getTasksByDate(date: String): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = databaseHandler.readableDatabase
        var task: Task? = null
        val cursor = db.query(
            TaskOpenHelper.TABLE_NAME, // Table name
            null, // Get all columns
            "${TaskOpenHelper.COLUMN_DATE} = ?", // WHERE clause (filter by date)
            arrayOf(date), // The date as the parameter
            null, // GROUP BY
            null, // HAVING
            null // ORDER BY
        )

        // Check if we have any results and iterate over the cursor
        if (cursor != null && cursor.moveToFirst()) {
            do {
                task = Task(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_NAME)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_DATE)),
                    time = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_TIME)),
                    desc = cursor.getString(cursor.getColumnIndexOrThrow(TaskOpenHelper.COLUMN_DESC))
                )
                tasks.add(task)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return tasks
    }
}
