package guillermo.lagos.spinner.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import guillermo.lagos.spinner.R
import guillermo.lagos.spinner.extension.inflate
import guillermo.lagos.spinner.view.ExpandableSelectionView

open class BasicStringAdapter(
    private val items: List<String>,
    var hint: String? = null
) : ExpandableItemAdapter {

    @DrawableRes
    var selectedStateResId: Int? = null
    @DrawableRes
    var collapsedStateResId: Int? = null
    @DrawableRes
    var expandedStateResId: Int? = null

    override fun inflateHeaderView(parent: ViewGroup): View {
        val view = parent.inflate(R.layout.spinner_header)
        val headerTV = view.findViewById<TextView>(R.id.headerTV)
        headerTV.hint = hint
        return view
    }

    override fun inflateItemView(parent: ViewGroup) =
        parent.inflate(R.layout.spinner_item)

    override fun bindItemView(itemView: View, position: Int, selected: Boolean) {
        val itemTV = itemView.findViewById<TextView>(R.id.itemNameTV)
        itemTV.text = items[position]
        itemView.findViewById<ImageView>(R.id.selectionIV)
            .setImageResource(selectedStateResId ?: R.drawable.spinner_ic_tick)
        itemView.findViewById<View>(R.id.selectionIV).isVisible = selected
    }

    override fun bindHeaderView(headerView: View, selectedIndices: List<Int>) {
        val headerTV = headerView.findViewById<TextView>(R.id.headerTV)
        if (selectedIndices.isEmpty()) {
            headerTV.text = null
        } else {
            headerTV.text = selectedIndices.joinToString { items[it] }

        }
    }

    override fun getItemsCount() = items.size



    override fun onViewStateChanged(headerView: View, state: ExpandableSelectionView.State) {
        val imageView = headerView.findViewById<ImageView>(R.id.listIndicatorIV)
        imageView.setImageResource(
            when (state) {
                ExpandableSelectionView.State.Expanded ->
                    expandedStateResId ?: R.drawable.spinner_ic_flecha_abajo
                ExpandableSelectionView.State.Collapsed ->
                    collapsedStateResId ?: R.drawable.spinner_ic_flecha_derecha
            }
        )
    }
}