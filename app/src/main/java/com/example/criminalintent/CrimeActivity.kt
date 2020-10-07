package com.example.criminalintent

import androidx.fragment.app.Fragment

class CrimeActivity:MainActivity() {
    fun createFragment(): Fragment {
        return CrimeFragment()
    }
}