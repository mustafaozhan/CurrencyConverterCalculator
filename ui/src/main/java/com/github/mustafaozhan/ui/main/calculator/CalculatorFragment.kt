/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.calculator

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mustafaozhan.basemob.util.Toast
import com.github.mustafaozhan.basemob.util.getNavigationResult
import com.github.mustafaozhan.basemob.util.reObserve
import com.github.mustafaozhan.basemob.util.showSnack
import com.github.mustafaozhan.basemob.view.fragment.BaseDBFragment
import com.github.mustafaozhan.ui.R
import com.github.mustafaozhan.ui.databinding.FragmentCalculatorBinding
import com.github.mustafaozhan.ui.main.MainData.Companion.KEY_BASE_CURRENCY
import com.github.mustafaozhan.ui.util.getImageResourceByName
import javax.inject.Inject

class CalculatorFragment : BaseDBFragment<FragmentCalculatorBinding>() {

    @Inject
    lateinit var calculatorViewModel: CalculatorViewModel

    private lateinit var calculatorAdapter: CalculatorAdapter

    override fun bind(container: ViewGroup?): FragmentCalculatorBinding =
        FragmentCalculatorBinding.inflate(layoutInflater, container, false)

    override fun onBinding(dataBinding: FragmentCalculatorBinding) {
        binding.vm = calculatorViewModel
        calculatorViewModel.getEvent().let {
            binding.event = it
            calculatorAdapter = CalculatorAdapter(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity()?.setSupportActionBar(binding.toolbarFragmentMain)
        initView()
        observeEffect()
        observeNavigationResult()
    }

    private fun observeNavigationResult() = getNavigationResult<String>(KEY_BASE_CURRENCY)
        ?.reObserve(viewLifecycleOwner, Observer {
            calculatorViewModel.verifyCurrentBase(it)
        })

    private fun observeEffect() = calculatorViewModel.effect
        .reObserve(viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                ErrorEffect -> showSnack(
                    requireView(),
                    R.string.rate_not_available_offline
                )
                FewCurrencyEffect -> showSnack(requireView(), R.string.choose_at_least_two_currency, R.string.select) {
                    navigate(
                        R.id.calculatorFragment,
                        CalculatorFragmentDirections.actionCalculatorFragmentToSettingsFragment()
                    )
                }
                MaximumInputEffect -> Toast.show(requireContext(), R.string.max_input)
                OpenBarEffect -> navigate(
                    R.id.calculatorFragment,
                    CalculatorFragmentDirections.actionCalculatorFragmentToBarBottomSheetDialogFragment()
                )
                is OfflineSuccessEffect -> viewEffect.date?.let {
                    Toast.show(requireContext(), getString(R.string.database_success_with_date, it))
                } ?: run {
                    Toast.show(requireContext(), R.string.database_success)
                }
                is ShowRateEffect -> showSnack(
                    requireView(),
                    viewEffect.text,
                    icon = requireContext().getImageResourceByName(viewEffect.name)
                )
            }
        })

    private fun initView() {
        binding.recyclerViewMain.apply {
            adapter = calculatorAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        with(calculatorViewModel) {
            state.currencyList.reObserve(viewLifecycleOwner, Observer {
                calculatorAdapter.submitList(it, data.currentBase)
            })
        }
    }
}