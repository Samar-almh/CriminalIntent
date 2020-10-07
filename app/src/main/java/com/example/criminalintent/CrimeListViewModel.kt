package com.example.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()
    init {
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
           // crimes += crime
            //  set requires police 1 in every 6
            crime.requiresPolice = (i % 6 == 0)
            crimes.add(crime)

        }
    }

}