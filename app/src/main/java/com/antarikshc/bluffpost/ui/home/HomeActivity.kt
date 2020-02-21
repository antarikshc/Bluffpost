package com.antarikshc.bluffpost.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.antarikshc.bluffpost.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        @SuppressLint("InlinedApi")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.WHITE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

//        val newsFragment = Class.forName("com.antarikshc.news.ui.NewsFragment")
//            .getConstructor()
//            .newInstance() as Fragment
//        val backstackName = newsFragment::class.java.simpleName
//
//        supportFragmentManager.beginTransaction().apply {
//            setReorderingAllowed(true)
//            replace(R.id.fragment_main_container, newsFragment, backstackName)
//            addToBackStack(backstackName)
//            commit()
//        }
    }
}
