/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.ui.main.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.mustafaozhan.basemob.view.adapter.BaseDBRecyclerViewAdapter
import mustafaozhan.github.com.mycurrencies.databinding.ItemCalculatorBinding
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.util.extension.toValidList

class CalculatorAdapter(
    private val calculatorEvent: CalculatorEvent
) : BaseDBRecyclerViewAdapter<Currency, ItemCalculatorBinding>(CalculatorDiffer()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CalculatorDBViewHolder(ItemCalculatorBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false)
    )

    fun submitList(list: MutableList<Currency>?, currentBase: String) =
        submitList(list.toValidList(currentBase))

    inner class CalculatorDBViewHolder(itemBinding: ItemCalculatorBinding) :
        BaseDBViewHolder<Currency, ItemCalculatorBinding>(itemBinding) {

        override fun onItemBind(item: Currency) = with(itemBinding) {
            this.item = item
            this.event = calculatorEvent
        }
    }

    class CalculatorDiffer : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = false
    }
}
