package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.lang.reflect.Array.newInstance

open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)


            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, CrimeListFragment.newInstance())
                .commit()
        }
}
