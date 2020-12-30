package com.illiarb.revoluttest.modules.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.di.AppProvider
import com.illiarb.revoluttest.di.Injectable
import com.illiarb.revoluttest.libs.tools.ConnectivityStatus
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.base.BaseActivity
import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.libs.ui.ext.exhaustive
import com.illiarb.revoluttest.libs.ui.ext.getColorAttr
import com.illiarb.revoluttest.libs.ui.widget.SnackbarController
import com.illiarb.revoluttest.modules.main.di.DaggerMainComponent
import timber.log.Timber
import javax.inject.Inject
import com.google.android.material.R as MaterialR
import com.illiarb.revoluttest.libs.ui.R as UiR

class MainActivity : BaseActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val viewModel by viewModels<MainViewModel>(factoryProducer = { viewModelFactory })
    private val snackbarController = SnackbarController()

    override fun inject(appProvider: AppProvider) {
        DaggerMainComponent.factory()
            .create(activity = this, dependencies = appProvider)
            .inject(this)
    }

    @SuppressLint("UnsafeExperimentalUsageWarning")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentManager.enableNewStateManager(/* enabled */ true)
    }

    override fun onStart() {
        super.onStart()

        viewModel.connectionState
            .observeOn(schedulerProvider.main)
            .subscribe(this::updateConnectionState) { Timber.e(it) }
            .addTo(onStopDisposable)
    }

    private fun updateConnectionState(state: ConnectivityStatus.State) {
        when (state) {
            ConnectivityStatus.State.CONNECTED -> {
                snackbarController.dismiss()
                snackbarController.showOrUpdateMessage(
                    getString(R.string.main_network_connected),
                    window.decorView,
                    Snackbar.LENGTH_LONG,
                    SnackbarController.Style.SUCCESS
                )
            }
            ConnectivityStatus.State.NOT_CONNECTED -> {
                snackbarController.showOrUpdateMessage(
                    getString(R.string.main_network_not_connected),
                    window.decorView,
                    Snackbar.LENGTH_INDEFINITE,
                    SnackbarController.Style.ERROR
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        it.setupSnackbarConnectivityAction()
                    }
                }
            }
        }.exhaustive
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun Snackbar.setupSnackbarConnectivityAction() {
        setAction(R.string.main_network_settings_panel) {
            startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
        }
    }
}