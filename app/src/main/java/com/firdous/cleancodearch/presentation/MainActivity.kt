package com.firdous.cleancodearch.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firdous.cleancodearch.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        print("Test gitRebase")
    }
}