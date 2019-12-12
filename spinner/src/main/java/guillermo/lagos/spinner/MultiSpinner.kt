package guillermo.lagos.spinner

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import guillermo.lagos.spinner.vista.VistaItemSpinner

@SuppressLint("NewApi")
class MultiSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VistaItemSpinner(context, attrs, defStyleAttr) {

    var listenerSeleccion: ((List<Int>) -> Unit)? = null
    val indicesSeleccionados: List<Int>
        get() = getIndicesItemSeleccionados()

    override fun itemClick(indices: Int) {
        if (haySeleccion(indices)) {
            itemDeseleccionado(indices)
        } else {
            itemSeleccionado(indices)
        }
        estadoListener()
    }

    override fun limpiarSeleccion() {
        super.limpiarSeleccion()
        listenerSeleccion?.invoke(emptyList())
    }

    fun indiceSeleccionado(indice: Int, estadoListener: Boolean = true) {
        if (!haySeleccion(indice)) {
            itemSeleccionado(indice)
            if (estadoListener) {
                estadoListener()
            }
        }
    }

    fun indiceDeseleccionado(indice: Int, estadoListener: Boolean = true) {
        if (haySeleccion(indice)) {
            itemDeseleccionado(indice)
            if (estadoListener) {
                estadoListener()
            }
        }
    }

    fun seleccionarIndices(indices: List<Int>, estadoListener: Boolean = true) {
        indices.filterNot(::haySeleccion)
            .forEach(::itemSeleccionado)
        if (estadoListener) {
            estadoListener()
        }
    }

    fun indicesDeseleccionados(indices: List<Int>, estadoListener: Boolean = true) {
        indices.filter(::haySeleccion)
            .forEach(::itemDeseleccionado)
        if (estadoListener) {
            estadoListener()
        }
    }

    private fun estadoListener() {
        listenerSeleccion?.invoke(indicesSeleccionados)
    }
}
