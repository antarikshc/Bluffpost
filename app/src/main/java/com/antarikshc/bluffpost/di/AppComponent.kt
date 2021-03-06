package com.antarikshc.bluffpost.di

import android.app.Application
import com.app.wedupp.di.ViewModelBuilder
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelBuilder::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

}