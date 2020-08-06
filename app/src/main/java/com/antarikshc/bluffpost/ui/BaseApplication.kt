package com.antarikshc.bluffpost.ui

import com.antarikshc.bluffpost.di.DaggerAppComponent
import com.google.android.play.core.splitcompat.SplitCompatApplication

class BaseApplication : SplitCompatApplication() {

    val appComponent = DaggerAppComponent.factory().create(this)

}