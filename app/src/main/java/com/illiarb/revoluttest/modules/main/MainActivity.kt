package com.illiarb.revoluttest.modules.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.databinding.ActivityMainBinding
import com.illiarb.revoluttest.di.AppProvider
import com.illiarb.revoluttest.di.Injectable
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.base.BaseActivity
import com.illiarb.revoluttest.libs.ui.ext.doOnApplyWindowInsets
import com.illiarb.revoluttest.libs.ui.ext.fromText
import com.illiarb.revoluttest.libs.ui.ext.setVisible
import com.illiarb.revoluttest.libs.ui.toolbar.FragmentToolbarLifecycleCallbacks
import com.illiarb.revoluttest.modules.main.di.DaggerMainComponent
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val viewBinding by viewBinding(ActivityMainBinding::bind, R.id.main_container)
    private val viewModel by viewModels<MainViewModel>(factoryProducer = { viewModelFactory })

    override fun inject(appProvider: AppProvider) {
        DaggerMainComponent.factory().create(
            /* activity */ this,
            /* dependencies */ appProvider
        ).inject(this)
    }

    @SuppressLint("UnsafeExperimentalUsageWarning")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentManager.enableNewStateManager(/* enabled */ true)

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            FragmentToolbarLifecycleCallbacks(viewBinding.mainToolbar),
            /* recursive */ false
        )

        viewBinding.mainNotConnectedLabel.doOnApplyWindowInsets { view, windowInsets, padding ->
            view.updatePadding(top = padding.top + windowInsets.systemWindowInsetTop)
        }

        viewBinding.mainAppBar.doOnApplyWindowInsets { view, windowInsets, padding ->
            view.updatePadding(top = padding.top + windowInsets.systemWindowInsetTop)
        }

        viewModel.connectionState
            .observeOn(schedulerProvider.main)
            .subscribe(
                { updateConnectionState(it) },
                { /* TODO: Log error */ Timber.e(it) }
            )
            .unsubscribeOnStop()

        ViewCompat.requestApplyInsets(viewBinding.mainContainer)
    }

    private fun updateConnectionState(state: MainViewModel.ConnectionState) {
        viewBinding.mainNotConnectedLabel.fromText(state.notConnectedText)
        viewBinding.mainNotConnectedLabel.setVisible(state.showLabel)
    }
}