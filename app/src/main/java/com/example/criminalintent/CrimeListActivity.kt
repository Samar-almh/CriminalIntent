package com.example.criminalintent

import androidx.fragment.app.Fragment

class CrimeListActivity:MainActivity() {
    fun createFragment(): Fragment {
        return CrimeListFragment()
    }
}