package com.illiarb.revoluttest.modules.home

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.illiarb.revoluttest.R
import com.illiarb.revoluttest.databinding.FragmentHomeBinding
import com.illiarb.revoluttest.di.AppProvider
import com.illiarb.revoluttest.di.Injectable
import com.illiarb.revoluttest.libs.tools.SchedulerProvider
import com.illiarb.revoluttest.libs.ui.base.BaseFragment
import com.illiarb.revoluttest.libs.ui.ext.addNavigationBarBottomPadding
import com.illiarb.revoluttest.libs.ui.toolbar.HasToolbar
import com.illiarb.revoluttest.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.revoluttest.modules.home.HomeViewModel.UiRate
import com.illiarb.revoluttest.modules.home.di.DaggerHomeComponent
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseFragment(R.layout.fragment_home), Injectable, HasToolbar {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>(factoryProducer = { viewModelFactory })
    private val delegatesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        object : DelegatesAdapter<UiRate>(
            ItemRateDelegate(
                {
                    viewModel.onItemClick(it)
                    viewBinding.homeRatesList.post {
                        viewBinding.homeRatesList.smoothScrollToPosition(0)
                    }
                },
                viewModel.amountChangedConsumer
            ),
            itemDiff = { old, new -> old.body == new.body }
        ) {
            override fun getItemId(position: Int): Long = items[position].body.hashCode().toLong()
        }
    }

    override fun inject(appProvider: AppProvider) =
        DaggerHomeComponent.factory().create(dependencies = appProvider).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.homeRatesList.let {
            it.layoutManager = LinearLayoutManager(view.context)
            it.adapter = delegatesAdapter.also { adapter -> adapter.setHasStableIds(true) }
            it.setHasFixedSize(true)
            it.addNavigationBarBottomPadding()
        }

        viewModel.ratesList
            .observeOn(schedulerProvider.main)
            .subscribe(
                { delegatesAdapter.submitList(it) },
                { Timber.e(it) }
            )
            .unsubscribeOnDestroyView()

        ViewCompat.requestApplyInsets(view)
    }

    override fun getToolbarOptions(): HasToolbar.ToolbarOptions {
        return HasToolbar.ToolbarOptions(
            isVisible = true,
            hasCloseButton = true,
            title = getString(R.string.home_toolbar_title)
        ) {
            requireActivity().onBackPressed()
        }
    }
}