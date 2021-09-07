package com.pyn.criminalintent.viewmodel

import androidx.lifecycle.ViewModel
import com.pyn.criminalintent.CrimeRepository
import com.pyn.criminalintent.bean.Crime
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    val crimesLiseLiveData = crimeRepository.getCrimes()

   init {

       GlobalScope.launch {
           for (i in 0 until 100) {
               val crime :Crime = Crime()
               crime.title = "Crime #$i"
               crime.isSolved = i % 2 != 0
               crime.requiresPolice = i % 2 == 0
               crimeRepository.insertCrimes(crime)
           }
       }
    }
}