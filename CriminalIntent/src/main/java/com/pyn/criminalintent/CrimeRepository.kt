package com.pyn.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.pyn.criminalintent.bean.Crime
import com.pyn.criminalintent.database.CrimeDatabase
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    fun getCrimes():LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id:UUID):LiveData<Crime?> = crimeDao.getCrime(id)

    fun insertCrimes(crime:Crime) = crimeDao.insertAll(crime)

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}