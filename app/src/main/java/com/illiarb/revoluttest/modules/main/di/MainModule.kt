package com.illiarb.revoluttest.modules.main.di

import androidx.lifecycle.ViewModel
import com.illiarb.revoluttest.libs.ui.di.ViewModelKey
import com.illiarb.revoluttest.modules.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMovieDetailsViewModel(viewModel: MainViewModel): ViewModel
}