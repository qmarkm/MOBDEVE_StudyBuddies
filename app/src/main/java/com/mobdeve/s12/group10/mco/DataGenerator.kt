package com.mobdeve.s12.group10.mco

//TODO: Remove this in final app
class DataGenerator {
    companion object {
        fun loadData(): ArrayList<StudyPact> {
            val data = ArrayList<StudyPact>()
            data.add(
                StudyPact(
                    "e",
                    "Temp 1",
                    1,
                    "2024-11-23 08:31 PM",
                    "DLSU Gokongwei",
                    "Study for finals",
                    ArrayList(List(10) { (1..100).random() })
                )
            )
            data.add(
                StudyPact(
                    "f",
                    "Temp 2",
                    1,
                    "2024-12-05 03:15 PM",
                    "UP Diliman",
                    "Project meeting",
                    ArrayList(List(10) { (1..100).random() })
                )
            )
            data.add(
                StudyPact(
                    "g",
                    "Temp 3",
                    1,
                    "2024-10-10 09:00 AM",
                    "Ateneo Library",
                    "Research workshop",
                    ArrayList(List(10) { (1..100).random() })
                )
            )
            data.add(
                StudyPact(
                    "w",
                    "Temp 4",
                    1,
                    "2024-11-15 07:45 PM",
                    "UST Main",
                    "Exam review",
                    ArrayList(List(10) { (1..100).random() })
                )
            )
            data.add(
                StudyPact(
                    "q",
                    "Temp 5",
                    1,
                    "2024-12-20 02:20 PM",
                    "FEU Study Hall",
                    "Lab assignment",
                    ArrayList(List(10) { (1..100).random() })
                )
            )
            data.add(
                StudyPact(
                    "i",
                    "Temp 6",
                    1,
                    "2024-09-30 05:55 PM",
                    "DLSU Gokongwei",
                    "Finals prep",
                    ArrayList(List(10) { (1..100).random() })
                )
            )
            data.add(
                StudyPact(
                    "n",
                    "Temp 7",
                    1,
                    "2024-11-01 06:30 PM",
                    "UP Diliman",
                    "Mock exams",
                    ArrayList(List(10) { (1..100).random() })
                )
            )


            return data
        }

        fun loadTasks(): ArrayList<Task> {
            val data = ArrayList<Task>()
            data.add(
                Task(
                    "MOBDEVE MCO2",
                    "2024-10-26",
                    "18:00",
                    "Frontend to a Mobile Phone"
                )
            )
            data.add(
                Task(
                    "MOBDEVE MCO3",
                    "2024-11-23",
                    "20:00",
                    "Complete functionality of a Mobile Phone"
                )
            )
            data.add(
                Task(
                    "CCDSALG MCO2",
                    "2024-10-29",
                    "12:00",
                    "Hash Table to multiple Strings"
                )
            )
            data.add(
                Task(
                    "CCAPDEV MCO1",
                    "2024-12-01",
                    "18:00",
                    "Proposal for the Website"
                )
            )

            return data
        }
    }
}