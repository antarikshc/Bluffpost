package com.antarikshc.bluffpost.di.home

import com.antarikshc.bluffpost.ui.home.HomeActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [HomeViewModelsModule::class]
)
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(activity: HomeActivity)

}