package com.eosr14.masksearch.ui.main

import com.eosr14.masksearch.common.base.BaseViewHolder
import com.eosr14.masksearch.databinding.ItemAutoCompleteBinding
import com.eosr14.masksearch.model.KaKaoKeyWord

class AutoCompleteViewHolder(private val binding: ItemAutoCompleteBinding) :
    BaseViewHolder<KaKaoKeyWord.Document>(binding.root) {
    override fun bind(item: KaKaoKeyWord.Document) {
        binding.document = item
    }
}