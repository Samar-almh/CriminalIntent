package com.example.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel :ViewModel() {

   /* val crimes= mutableListOf<Crime>()
    init {
        for(i in 0 until  100){
            val crime=Crime()
            crime.title="crime #$i"
            crime.isSolved=i%2==0
            crime.requiresPolice=if(i%2!=0){
                1
            }
            else
                2
            crimes+=crime
        }
    }*/
   private val crimeRepository = CrimeRepository.get()
   val crimeListLiveData = crimeRepository.getCrimes()
   fun addCrime(crime: Crime) {
      crimeRepository.addCrime(crime)
   }


}