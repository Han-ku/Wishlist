package com.example.p2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, ListFragment())
            .commit()

        if (intent?.extras != null) {
            val fragmentToLoad = intent?.extras?.getString("fragmentToLoad")
            val productId = intent?.extras?.getInt("productId", -1)

            if (fragmentToLoad == "DetailsProductFragment" && productId != -1) {
                val detailsProductFragment = DetailsProductFragment.newInstance(productId!!)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, detailsProductFragment)
                    .commit()
            }
        }
    }
}