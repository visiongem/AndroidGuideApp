package com.pyn.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.flayout_fragment_container)
        if(currentFragment == null){
            val crimeFragment = CrimeFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.flayout_fragment_container, crimeFragment)
                .commit()
        }
    }
}