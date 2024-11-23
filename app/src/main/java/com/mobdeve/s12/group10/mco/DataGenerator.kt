package com.mobdeve.s12.group10.mco

//TODO: Remove this in final app
class DataGenerator {
    companion object {
        fun loadData(): ArrayList<StudyPact> {
            val data = ArrayList<StudyPact>()
            data.add(
                StudyPact(
                    1,
                    "Temp 1",
                    1,
                    "2024-11-23 08:31 PM",
                    "DLSU Gokongwei",
                    "Study for finals",
                    "1, 2, 3"
                )
            )
            data.add(
                StudyPact(
                    2,
                    "Temp 2",
                    1,
                    "2024-12-05 03:15 PM",
                    "UP Diliman",
                    "Project meeting",
                    "4, 5, 6"
                )
            )
            data.add(
                StudyPact(
                    3,
                    "Temp 3",
                    1,
                    "2024-10-10 09:00 AM",
                    "Ateneo Library",
                    "Research workshop",
                    "7, 8, 9"
                )
            )
            data.add(
                StudyPact(
                    4,
                    "Temp 4",
                    1,
                    "2024-11-15 07:45 PM",
                    "UST Main",
                    "Exam review",
                    "10, 11, 12"
                )
            )
            data.add(
                StudyPact(
                    5,
                    "Temp 5",
                    1,
                    "2024-12-20 02:20 PM",
                    "FEU Study Hall",
                    "Lab assignment",
                    "13, 14, 15"
                )
            )
            data.add(
                StudyPact(
                    6,
                    "Temp 6",
                    1,
                    "2024-09-30 05:55 PM",
                    "DLSU Gokongwei",
                    "Finals prep",
                    "1, 3, 5"
                )
            )
            data.add(
                StudyPact(
                    7,
                    "Temp 7",
                    1,
                    "2024-11-01 06:30 PM",
                    "UP Diliman",
                    "Mock exams",
                    "2, 4, 6"
                )
            )


            return data
        }
    }
}