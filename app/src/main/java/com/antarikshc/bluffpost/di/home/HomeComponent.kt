package com.antarikshc.bluffpost.di.home

import com.antarikshc.bluffpost.ui.home.HomeActivity
import com.antarikshc.bluffpost.ui.home.HomeFragment
import dagger.Subcomponent

@Subcomponent
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(activity: HomeActivity)
    fun inject(homeFragment: HomeFragment)

}