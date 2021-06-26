package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.R
import kotlinx.android.synthetic.main.item_layout.view.*

class CharacterAdapter(
    val context: Context,
    private val characterList: ArrayList<String>
) : RecyclerView.Adapter<CharacterAdapter.DataViewHolder>() {

    var selectedCharacters: ArrayList<String> = ArrayList()

    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, character: String, selectedCharacters: ArrayList<String>) {

            itemView.character_title_tv.text = character

            itemView.setOnClickListener {
                if (!it.isSelected) {
                    itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_selected_bg)
                    itemView.character_title_tv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    selectedCharacters.add(character)

                    it.isSelected = true
                } else {
                    itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_bg)
                    itemView.character_title_tv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grey
                        )
                    )

                    selectedCharacters.remove(character)

                    it.isSelected = false
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = characterList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(context, characterList[position], selectedCharacters)

    fun addData(list: List<String>) {
        characterList.addAll(list)
    }

    open fun getSelectedItems(): ArrayList<String> {
        return selectedCharacters
    }
}