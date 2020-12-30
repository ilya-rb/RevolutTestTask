package com.illiarb.revoluttest.modules.home

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.databinding.FragmentHomeBinding
import com.illiarb.revoluttest.di.AppProvider
import com.illiarb.revoluttest.di.Injectable
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.base.BaseFragment
import com.illiarb.revoluttest.libs.ui.ext.addNavigationBarBottomPadding
import com.illiarb.revoluttest.libs.ui.ext.addStatusBarTopPadding
import com.illiarb.revoluttest.libs.ui.ext.addTo
import com.illiarb.revoluttest.libs.ui.ext.exhaustive
import com.illiarb.revoluttest.libs.ui.widget.SnackbarController
import com.illiarb.revoluttest.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.revoluttest.libs.ui.widget.recyclerview.StatefulRecyclerView.State.CONTENT
import com.illiarb.revoluttest.libs.ui.widget.recyclerview.StatefulRecyclerView.State.EMPTY
import com.illiarb.revoluttest.libs.ui.widget.recyclerview.StatefulRecyclerView.State.ERROR
import com.illiarb.revoluttest.libs.util.Async
import com.illiarb.revoluttest.modules.home.di.DaggerHomeComponent
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseFragment(R.layout.fragment_home), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { viewModelFactory })
    private val snackbarController = SnackbarController()

    private val delegatesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        object : DelegatesAdapter<UiRate>(
            ItemRateDelegate({ viewModel.onItemClick(it) }, viewModel.amountChangedConsumer),
            itemDiff = { old, new -> old.code == new.code },
            changePayload = { old, new -> RatesChangedPayload.create(old, new) }
        ) {
            override fun getItemId(position: Int): Long = items[position].code.hashCode().toLong()
        }
    }

    override fun inject(appProvider: AppProvider) =
        DaggerHomeComponent.factory().create(dependencies = appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.homeAppBar.addStatusBarTopPadding()
        viewBinding.homeToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        viewBinding.homeRatesList.setAnimationViewRawRes(R.raw.anim_exchange)
        viewBinding.homeRatesList.recyclerView {
            it.layoutManager = LinearLayoutManager(view.context)
            it.adapter = delegatesAdapter.also { adapter -> adapter.setHasStableIds(true) }
            it.setHasFixedSize(true)
        }
        viewBinding.homeRatesList.addNavigationBarBottomPadding()

        viewModel.ratesList
            .observeOn(schedulerProvider.main)
            .subscribe(
                {
                    when (it) {
                        is Async.Uninitialized, is Async.Loading, is Async.Fail ->
                            if (it is Async.Fail) {
                                viewBinding.homeRatesList.moveToState(ERROR)
                            } else {
                                viewBinding.homeRatesList.moveToState(EMPTY)
                            }
                        is Async.Success -> {
                            delegatesAdapter.submitList(it())
                            viewBinding.homeRatesList.moveToState(CONTENT)
                        }
                    }.exhaustive
                },
                { Timber.e(it) }
            )
            .addTo(onDestroyViewDisposable)

        viewModel.emptyViewText
            .observeOn(schedulerProvider.main)
            .distinctUntilChanged()
            .subscribe(viewBinding.homeRatesList::setAnimationViewCaption) { Timber.e(it) }
            .addTo(onDestroyViewDisposable)

        viewModel.errorMessages
            .observeOn(schedulerProvider.main)
            .subscribe(this::showErrorMessage) { Timber.e(it) }
            .addTo(onDestroyViewDisposable)

        ViewCompat.requestApplyInsets(view)
    }

    private fun showErrorMessage(message: String?) {
        message?.let {
            snackbarController.showOrUpdateMessage(
                message,
                viewBinding.root,
                Snackbar.LENGTH_LONG,
                SnackbarController.Style.ERROR
            )
        }
    }
}