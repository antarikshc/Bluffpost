package com.antarikshc.bluffpost.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.antarikshc.bluffpost.R
import com.antarikshc.bluffpost.di.home.HomeComponent
import com.antarikshc.bluffpost.ui.BaseApplication

class HomeActivity : AppCompatActivity() {

    lateinit var homeComponent: HomeComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creating of the Home graph using the application graph
        homeComponent = (application as BaseApplication).appComponent.homeComponent().create()
            .apply { inject(this@HomeActivity) } // Make Dagger instantiate @Inject fields in Activity

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        @SuppressLint("InlinedApi")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.WHITE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}
