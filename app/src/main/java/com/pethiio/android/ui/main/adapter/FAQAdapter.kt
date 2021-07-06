package com.pethiio.android.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.R
import com.pethiio.android.data.model.settings.FAQResponse
import kotlinx.android.synthetic.main.item_expanded.view.*
import kotlinx.android.synthetic.main.message_list_item.view.*
import net.cachapa.expandablelayout.ExpandableLayout.OnExpansionUpdateListener


class FAQAdapter(
    private val faqs: List<FAQResponse>
) : RecyclerView.Adapter<FAQAdapter.DataViewHolder>() {


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnExpansionUpdateListener {
        private val UNSELECTED = -1
        private var selectedItem = UNSELECTED


        @SuppressLint("SetTextI18n")
        fun bind(faq: FAQResponse) {



            itemView.titleTextView.text = ""+faq.id+". "+faq.question
            itemView.description.text =faq.answer


        }

        override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
//            if (state == ExpandableLayout.State.EXPANDING) {
//                smoothScrollToPosition(adapterPosition)
//            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_expanded, parent,
                false
            )
        )

    override fun getItemCount(): Int = faqs.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {


        holder.bind(faqs[position])

        val isExpanded: Boolean = faqs[position].isExpanded
        holder.itemView.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.itemView.titleTextView.setOnClickListener {
            val movie: FAQResponse = faqs[position]
            movie.isExpanded = (!movie.isExpanded)
            notifyItemChanged(position)
        }
    }


}