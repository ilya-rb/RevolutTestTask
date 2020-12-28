package com.illiarb.revoluttest.modules.home.di

import androidx.lifecycle.ViewModel
import com.illiarb.revoluttest.libs.ui.di.ViewModelKey
import com.illiarb.revoluttest.modules.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}