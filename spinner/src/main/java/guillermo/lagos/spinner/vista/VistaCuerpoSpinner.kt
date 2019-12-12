package guillermo.lagos.spinner.vista

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import guillermo.lagos.spinner.utilidades.cerrar
import guillermo.lagos.spinner.utilidades.abrir


class VistaCuerpoSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var animacion = false
    var alturaMaxima: Int = Int.MAX_VALUE

    //Setea el maximo de ancho y alto para la vista.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newHeightSpec = when {
            animacion -> heightMeasureSpec
            else -> MeasureSpec.makeMeasureSpec(alturaMaxima, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    fun abrir(duracionAnimacion: Long) {
        this.animacion = true
        this.abrir(alturaMaxima, duracionAnimacion) {
            this.animacion = false
        }
    }

    fun cerrar(duracionAnimacion: Long) {
        this.animacion = true
        this.cerrar(duracionAnimacion) {
            this.animacion = false
        }
    }
}
