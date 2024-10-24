package com.mobdeve.s12.group10.mco

//TODO: Remove this in final app
class DataGenerator {
    companion object {
        fun loadData(): ArrayList<StudyPact> {
            val data = ArrayList<StudyPact>()
            data.add(StudyPact("Temp 1"))
            data.add(StudyPact("Temp 2"))
            data.add(StudyPact("Temp 3"))
            data.add(StudyPact("Temp 4"))
            data.add(StudyPact("Temp 5"))
            data.add(StudyPact("Temp 6"))
            data.add(StudyPact("Temp 7"))
            return data
        }
    }
}