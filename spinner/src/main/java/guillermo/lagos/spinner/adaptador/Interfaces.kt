package guillermo.lagos.spinner.adaptador

import android.view.View
import android.view.ViewGroup
import guillermo.lagos.spinner.vista.VistaItemSpinner

interface Interfaces {
    fun inflateHeaderView(parent: ViewGroup): View

    fun inflateItemView(parent: ViewGroup): View

    fun bindItemView(itemView: View, position: Int, selected: Boolean)

    fun bindHeaderView(headerView: View, selectedIndices: List<Int>)

    fun onViewStateChanged(headerView: View, state: VistaItemSpinner.Estado)

    fun getItemsCount(): Int

}