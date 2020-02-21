package com.antarikshc.news.di

import com.antarikshc.bluffpost.di.AppComponent
import com.antarikshc.news.ui.NewsFragment
import dagger.Component

@NewsScope
@Component(dependencies = [AppComponent::class], modules = [NewsModule::class])
interface NewsComponent {

    @Component.Factory
    interface Factory {
        // Takes an instance of AppComponent when creating an instance
        fun create(appComponent: AppComponent, module: NewsModule): NewsComponent
    }

    fun inject(newsFragment: NewsFragment)

}