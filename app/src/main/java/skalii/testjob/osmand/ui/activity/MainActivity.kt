package skalii.testjob.osmand.ui.activity


import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

import by.kirich1409.viewbindingdelegate.viewBinding

import skalii.testjob.osmand.R
import skalii.testjob.osmand.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind, R.id.activity_main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar
            ?.setBackgroundDrawable(
                ColorDrawable(
                    ResourcesCompat.getColor(resources, R.color.action_bar, null)
                )
            )

    }

}