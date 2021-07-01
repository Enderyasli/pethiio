package com.pethiio.android.ui.main.adapter.CardStack

import androidx.recyclerview.widget.DiffUtil
import com.pethiio.android.data.model.member.PetSearchResponse

class CardSackDiffCallback (
    private val old: List<PetSearchResponse>,
    private val new: List<PetSearchResponse>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].petId == new[newPosition].petId
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition] == new[newPosition]
    }

}