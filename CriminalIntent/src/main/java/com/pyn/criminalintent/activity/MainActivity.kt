package com.pyn.criminalintent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pyn.criminalintent.R
import com.pyn.criminalintent.databinding.ActivityMainBinding
import com.pyn.criminalintent.fragment.CrimeFragment
import com.pyn.criminalintent.fragment.CrimeListFragment
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CrimeListFragment.CallBacks {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.flayout_fragment_container)
        if (currentFragment == null) {
            val crimeListFragment = CrimeListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.flayout_fragment_container, crimeListFragment)
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: UUID) {
        Log.d(TAG, "MainActivity.onCrimeSelected : $crimeId")
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.flayout_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}