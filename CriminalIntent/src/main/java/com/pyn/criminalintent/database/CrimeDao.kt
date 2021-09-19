package com.pyn.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pyn.criminalintent.bean.Crime
import java.util.*

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id IN (:id)")
    fun getCrime(id: UUID): LiveData<Crime?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrime(crime: Crime)

    @Update
    fun updateCrime(crime: Crime)

    @Query("DELETE FROM crime")
    suspend fun deleteAll()
}