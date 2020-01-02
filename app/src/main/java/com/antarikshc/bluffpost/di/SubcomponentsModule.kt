package com.antarikshc.bluffpost.di

import com.antarikshc.bluffpost.di.home.HomeComponent
import dagger.Module

@Module(subcomponents = [HomeComponent::class])
object SubcomponentsModule {}