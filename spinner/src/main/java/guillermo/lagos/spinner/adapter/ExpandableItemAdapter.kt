package guillermo.lagos.spinner.adapter

import android.view.View
import android.view.ViewGroup
import guillermo.lagos.spinner.view.ExpandableSelectionView

interface ExpandableItemAdapter {
    fun inflateHeaderView(parent: ViewGroup): View

    fun inflateItemView(parent: ViewGroup): View

    fun bindItemView(itemView: View, position: Int, selected: Boolean)

    fun bindHeaderView(headerView: View, selectedIndices: List<Int>)

    fun onViewStateChanged(headerView: View, state: ExpandableSelectionView.State)

    fun getItemsCount(): Int

}