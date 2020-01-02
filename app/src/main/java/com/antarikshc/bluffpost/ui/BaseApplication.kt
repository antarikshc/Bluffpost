package com.antarikshc.bluffpost.ui

import android.app.Application
import com.antarikshc.bluffpost.di.DaggerAppComponent

class BaseApplication : Application() {

    val appComponent = DaggerAppComponent.factory().create(this)

}