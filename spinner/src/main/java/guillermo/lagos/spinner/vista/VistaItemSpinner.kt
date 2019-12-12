package guillermo.lagos.spinner.vista

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import guillermo.lagos.spinner.R
import guillermo.lagos.spinner.adaptador.Interfaces
import guillermo.lagos.spinner.adaptador.AdaptadorItemSpinner
import guillermo.lagos.spinner.utilidades.inflate

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
abstract class VistaItemSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val contenedor: LinearLayout
    private val cuerpoSpinner: VistaCuerpoSpinner
    private val txt_error: TextView
    private var defaultSpinner: View

    private var estadoSpinner: Estado = Estado.Cerrado
    private var bgSpinner: Drawable? = getDrawable(context, R.drawable.spinner_bg)
    private var scrollVisible: Boolean = true
    private var barraVisible: Boolean = true
    private var colorBarra: Int = Color.parseColor("#668b999f")
    private var maximaAltura: Int = Int.MAX_VALUE
    private var duracionAnimacion: Long = DEFAULT_ANIMATION_DURATION
    private var indicesItemSeleccionados: MutableList<Int> = mutableListOf()

    private var interfaces: Interfaces? = null
    private var adaptador: AdaptadorItemSpinner? = null

    init {
        attrs?.let { obtenerAtributosVista(it) }

        this.orientation = VERTICAL

        contenedor = LinearLayout(context)
        contenedor.orientation = VERTICAL
        contenedor.background = bgSpinner

        val recyclerStyle = when {
            scrollVisible -> R.style.ExpandableRecyclerView_Scrollbars
            else -> R.style.ExpandableRecyclerView
        }
        cuerpoSpinner =
            VistaCuerpoSpinner(ContextThemeWrapper(context, recyclerStyle))
        cuerpoSpinner.alturaMaxima = maximaAltura

        txt_error = inflate(R.layout.spinner_error) as TextView
        defaultSpinner = View(context)

        this.addView(contenedor)
        this.addView(txt_error)
    }

    fun setAdaptador(interfaces: Interfaces) {
        this.interfaces = interfaces
        val recyclerAdapter = AdaptadorItemSpinner(
            interfaces,
            ::itemClick,
            ::haySeleccion
        ).also {
            it.showDividers = barraVisible
            it.dividerColor = colorBarra
        }
        setAdaptador(recyclerAdapter)
        agregarContenido(interfaces)
        inicializarEstado()
    }

    fun setError(errorStr: String?) {
        txt_error.isGone = (errorStr == null)
        txt_error.text = errorStr
    }

    fun listenerEstado(state: Estado) {
        if (estadoSpinner == state)
            return
        listenerEstado()
    }

    internal fun getIndicesItemSeleccionados(): List<Int> = indicesItemSeleccionados

    open fun limpiarSeleccion() {
        indicesItemSeleccionados.clear()
        interfaces?.bindHeaderView(defaultSpinner, indicesItemSeleccionados)
        adaptador?.notifyDataSetChanged()
    }

    private fun obtenerAtributosVista(attrs: AttributeSet) {
        context.withStyledAttributes(
            attrs,
            R.styleable.VistaItemSpinner, 0, 0
        ) {
            bgSpinner = getDrawable(R.styleable.VistaItemSpinner_background) ?: bgSpinner
            maximaAltura = getLayoutDimension(
                R.styleable.VistaItemSpinner_maximaAltura,
                maximaAltura
            )
            barraVisible = getBoolean(
                R.styleable.VistaItemSpinner_barraVisible,
                barraVisible
            )
            scrollVisible = getBoolean(
                R.styleable.VistaItemSpinner_scrollVisible,
                scrollVisible
            )
            colorBarra = getColor(
                R.styleable.VistaItemSpinner_colorBarraDivisora,
                colorBarra
            )
            duracionAnimacion = getInteger(
                R.styleable.VistaItemSpinner_duracionAnimacion,
                duracionAnimacion.toInt()
            ).toLong()
        }
    }

    private fun inicializarEstado() {
        this.estadoSpinner = Estado.Cerrado
        interfaces?.bindHeaderView(defaultSpinner, indicesItemSeleccionados)
        interfaces?.onViewStateChanged(
            defaultSpinner,
            estadoSpinner
        )
        cuerpoSpinner.isGone = true
    }

    private fun agregarContenido(interfaces: Interfaces) {
        defaultSpinner = interfaces.inflateHeaderView(this)
        defaultSpinner.setOnClickListener { defaultSpinnerClick() }
        contenedor.removeAllViews()
        contenedor.addView(defaultSpinner)
        contenedor.addView(cuerpoSpinner)
    }

    private fun setAdaptador(adaptadorItemSpinner: AdaptadorItemSpinner) {
        this.adaptador = adaptadorItemSpinner
        val linearLayoutManager = LinearLayoutManager(context)
        cuerpoSpinner.adapter = adaptadorItemSpinner
        cuerpoSpinner.itemAnimator = null
        cuerpoSpinner.layoutManager = linearLayoutManager
    }

    private fun defaultSpinnerClick() {
        listenerEstado()
    }

    private fun listenerEstado() {
        when (estadoSpinner) {
            is Estado.Abierto -> cerrado()
            is Estado.Cerrado -> abierto()
        }
        estadoSpinner = !estadoSpinner
        interfaces?.onViewStateChanged(defaultSpinner, estadoSpinner)
    }

    private fun abierto() {
        cuerpoSpinner.scrollToPosition(0)
        cuerpoSpinner.abrir(duracionAnimacion)
    }

    private fun cerrado() {
        cuerpoSpinner.cerrar(duracionAnimacion)
    }

    abstract fun itemClick(indice: Int)

    protected fun haySeleccion(indice: Int) = indicesItemSeleccionados.contains(indice)

    protected fun itemSeleccionado(indice: Int) {
        indicesItemSeleccionados.add(indice)
        interfaces?.bindHeaderView(defaultSpinner, indicesItemSeleccionados)
        adaptador?.notifyItemChanged(indice)
    }

    protected fun itemDeseleccionado(indice: Int) {
        indicesItemSeleccionados.remove(indice)
        interfaces?.bindHeaderView(defaultSpinner, indicesItemSeleccionados)
        adaptador?.notifyItemChanged(indice)
    }

    sealed class Estado {
        object Abierto : Estado()
        object Cerrado : Estado()

        operator fun not(): Estado =
            when (this) {
                Abierto -> Cerrado
                Cerrado -> Abierto
            }
    }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION: Long = 300L
    }
}