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
import com.illiarb.revoluttest.libs.ui.ext.addStatusBarTopPadding
import com.illiarb.revoluttest.libs.ui.widget.recyclerview.DelegatesAdapter
import com.illiarb.revoluttest.modules.home.HomeViewModel.UiRate
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

    private val delegatesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        object : DelegatesAdapter<UiRate>(
            ItemRateDelegate({ viewModel.onItemClick(it) }, viewModel.amountChangedConsumer),
            itemDiff = { old, new -> old.code == new.code },
            changePayload = { old, new -> createRatesChangePayload(old, new) }
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

    private fun createRatesChangePayload(old: UiRate, new: UiRate): Bundle? {
        val diffBundle = Bundle()
        if (old.imageUrl != new.imageUrl) {
            diffBundle.putBoolean(ItemRateDelegate.PAYLOAD_NEW_IMAGE, true)
        }
        if (old.rate != new.rate) {
            diffBundle.putBoolean(ItemRateDelegate.PAYLOAD_NEW_RATE, true)
        }

        return if (diffBundle.isEmpty) null else diffBundle
    }
}