package guillermo.lagos.spinner

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import guillermo.lagos.spinner.vista.VistaItemSpinner

@SuppressLint("NewApi")
class UniSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VistaItemSpinner(context, attrs, defStyleAttr) {

    var cerrarAlSeleccionar: Boolean = true

    var listenerSeleccion: ((Int?) -> Unit)? = null

    val indiceSeleccionado: Int?
        get() = getIndicesItemSeleccionados().firstOrNull()

    override fun itemClick(indice: Int) {
        val selectedItems = getIndicesItemSeleccionados()
        if (haySeleccion(indice)) {
            itemDeseleccionado(indice)
            estadoListener(null)
        } else {
            if (selectedItems.isNotEmpty())
                itemDeseleccionado(selectedItems.first())
            itemSeleccionado(indice)
            estadoListener(indice)
        }
        if (cerrarAlSeleccionar)
            listenerEstado(Estado.Cerrado)
    }

    fun seleccionarIndice(indice: Int, estadoListener: Boolean = true) {
        if (!haySeleccion(indice)) {
            val itemsSeleccionados = getIndicesItemSeleccionados()
            if (itemsSeleccionados.isNotEmpty()) {
                itemDeseleccionado(itemsSeleccionados.first())
            }
            itemSeleccionado(indice)
            if (estadoListener) {
                estadoListener(indice)
            }
        }
    }

    override fun limpiarSeleccion() {
        super.limpiarSeleccion()
        estadoListener(null)
    }

    private fun estadoListener(indices: Int?) {
        listenerSeleccion?.invoke(indices)
    }


}