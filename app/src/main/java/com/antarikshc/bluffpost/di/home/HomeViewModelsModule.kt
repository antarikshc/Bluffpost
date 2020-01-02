package com.antarikshc.bluffpost.di.home

import androidx.lifecycle.ViewModel
import com.antarikshc.bluffpost.ui.home.HomeVM
import com.app.wedupp.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    abstract fun bindHomeVM(viewModel: HomeVM): ViewModel

}