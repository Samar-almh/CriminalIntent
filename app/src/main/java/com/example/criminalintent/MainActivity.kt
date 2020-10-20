package com.example.criminalintent

import CrimeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import java.lang.reflect.Array.newInstance
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),CrimeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance()

            supportFragmentManager
                . beginTransaction ()
                .add(R.id.fragment_container, fragment)

                .commit()
        }
    }
    override fun onCrimeSelected(crimeId: UUID) {
       // Log.d(TAG, "MainActivity.onCrimeSelected: $crimeId")
       // val fragment = CrimeFragment()
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}