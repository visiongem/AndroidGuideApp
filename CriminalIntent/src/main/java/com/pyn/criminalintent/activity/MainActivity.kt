package com.pyn.criminalintent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pyn.criminalintent.fragment.CrimeFragment
import com.pyn.criminalintent.R
import com.pyn.criminalintent.fragment.CrimeListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.flayout_fragment_container)
        if(currentFragment == null){
            val crimeListFragment = CrimeListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.flayout_fragment_container, crimeListFragment)
                .commit()
        }
    }
}