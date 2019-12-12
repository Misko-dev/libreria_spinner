package guillermo.lagos.spinner.adaptador

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import guillermo.lagos.spinner.R
import guillermo.lagos.spinner.utilidades.inflate
import guillermo.lagos.spinner.vista.VistaItemSpinner

open class AdaptadorSpinner(
    private val items: List<String>,
    var hint: String? = null
) : Interfaces {

    @DrawableRes
    var estadoSeleccionResID: Int? = null
    @DrawableRes
    var estadoCerradoResID: Int? = null
    @DrawableRes
    var estadoAbiertoResID: Int? = null

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
        itemView.findViewById<ImageView>(R.id.selectionIV)
            .setImageResource(estadoSeleccionResID ?: R.drawable.spinner_ic_tick)
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
        val ic_default = headerView.findViewById<ImageView>(R.id.listIndicatorIV)
        ic_default.setImageResource(
            when (state) {
                VistaItemSpinner.Estado.Abierto ->
                    estadoAbiertoResID ?: R.drawable.spinner_ic_flecha_abajo
                VistaItemSpinner.Estado.Cerrado ->
                    estadoCerradoResID ?: R.drawable.spinner_ic_flecha_derecha
            }
        )
    }
}