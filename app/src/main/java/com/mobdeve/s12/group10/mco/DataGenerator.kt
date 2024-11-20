package com.mobdeve.s12.group10.mco

//TODO: Remove this in final app
class DataGenerator {
    companion object {
        fun loadData(): ArrayList<StudyPact> {
            val data = ArrayList<StudyPact>()
            data.add(
                StudyPact(
                    "Temp 1",
                    "2024-11-23 08:31 PM",
                    "DLSU Gokongwei",
                    "Finals study session",
                    "1, 2, 3"
                )
            )
            data.add(
                StudyPact(
                    "Temp 2",
                    "2024-12-05 03:15 PM",
                    "UP Diliman",
                    "Group project meeting",
                    "4, 5, 6"
                )
            )
            data.add(
                StudyPact(
                    "Temp 3",
                    "2024-10-10 09:00 AM",
                    "Ateneo Library",
                    "Research workshop",
                    "7, 8, 9"
                )
            )
            data.add(
                StudyPact(
                    "Temp 4",
                    "2024-11-15 07:45 PM",
                    "UST Main",
                    "Exam review session",
                    "10, 11, 12"
                )
            )
            data.add(
                StudyPact(
                    "Temp 5",
                    "2024-12-20 02:20 PM",
                    "FEU Study Hall",
                    "Lab assignment help",
                    "13, 14, 15"
                )
            )
            data.add(
                StudyPact(
                    "Temp 6",
                    "2024-09-30 05:55 PM",
                    "DLSU Gokongwei",
                    "Finals preparation",
                    "1, 3, 5"
                )
            )
            data.add(
                StudyPact(
                    "Temp 7",
                    "2024-11-01 06:30 PM",
                    "UP Diliman",
                    "Mock exams",
                    "2, 4, 6"
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