package guillermo.lagos.spinner.adaptador

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.mikepenz.iconics.view.IconicsButton
import guillermo.lagos.spinner.R
import guillermo.lagos.spinner.utilidades.inflate
import guillermo.lagos.spinner.vista.VistaItemSpinner

open class AdaptadorSpinner(
    private val items: List<String>,
    var hint: String? = null
) : Interfaces {


    var estadoSeleccionResID: String? = null

    var estadoCerradoResID: String? = null

    var estadoAbiertoResID: String? = null

    override fun inflateHeaderView(parent: ViewGroup): View {
        val view = parent.inflate(R.layout.spinner_default)
        val txt_titulo_default = view.findViewById<TextView>(R.id.headerTV)
        txt_titulo_default.hint = hint
        return view

    }

    override fun inflateItemView(parent: ViewGroup) =
        parent.inflate(R.layout.spinner_item)

    override fun bindItemView(itemView: View, position: Int, selected: Boolean) {
        val txt_titulo_item = itemView.findViewById<TextView>(R.id.itemNameTV)
        txt_titulo_item.text = items[position]
        itemView.findViewById<IconicsButton>(R.id.selectionIV)
            .text = (estadoSeleccionResID ?: "{faw-check}")
        itemView.findViewById<View>(R.id.selectionIV).isVisible = selected
    }

    override fun bindHeaderView(headerView: View, selectedIndices: List<Int>) {
        val txt_titulo_default = headerView.findViewById<TextView>(R.id.headerTV)
        if (selectedIndices.isEmpty()) {
            txt_titulo_default.text = null
        } else {
            txt_titulo_default.text = selectedIndices.joinToString { items[it] }

        }
    }

    override fun getItemsCount() = items.size


    override fun onViewStateChanged(headerView: View, state: VistaItemSpinner.Estado) {
        val ic_default = headerView.findViewById<IconicsButton>(R.id.listIndicatorIV)
        ic_default.text = when (state) {
            VistaItemSpinner.Estado.Abierto ->
                estadoAbiertoResID ?: "{angle-right}"
            VistaItemSpinner.Estado.Cerrado ->
                estadoCerradoResID ?: "{angle-down}"
        }
    }
}