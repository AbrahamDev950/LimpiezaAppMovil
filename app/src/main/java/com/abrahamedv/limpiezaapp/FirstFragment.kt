
package com.abrahamedv.limpiezaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abrahamedv.limpiezaapp.databinding.FragmentFirstBinding
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.abrahamedv.limpiezaapp.models.Producto
import com.abrahamedv.limpiezaapp.services.ProductoService
import com.abrahamedv.limpiezaapp.services.AuthService
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val productoService = ProductoService()
    private val binding get() = _binding!!
    private val authService = AuthService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Acción temporal del botón.
        binding.buttonAgregarProducto.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Aquí registraremos un producto",
                Toast.LENGTH_SHORT
            ).show()
        }

        authService.autenticarAnonimamente(
            alCompletar = {
                if (!isAdded || _binding == null) {
                    return@autenticarAnonimamente
                }

                val usuario = FirebaseAuth.getInstance().currentUser

                if (usuario == null) {
                    binding.textEstadoProductos.visibility = View.VISIBLE
                    binding.textEstadoProductos.text =
                        "No hay una sesión activa."
                    return@autenticarAnonimamente
                }

                cargarProductos()
            },
            alError = { error ->
                if (!isAdded || _binding == null) {
                    return@autenticarAnonimamente
                }

                binding.textEstadoProductos.visibility = View.VISIBLE
                binding.textEstadoProductos.text =
                    "Error de autenticación: ${error.message}"
            }
        )

    }


    private fun cargarProductos() {
        binding.textEstadoProductos.text = "Cargando productos..."

        productoService.obtenerProductos(
            alCompletar = { productos ->
                if (!isAdded || _binding == null) return@obtenerProductos

                binding.contenedorProductos.removeAllViews()
                binding.textTotalProductos.text = productos.size.toString()

                if (productos.isEmpty()) {
                    binding.textEstadoProductos.text =
                        "Todavía no hay productos registrados."
                    binding.textEstadoProductos.visibility = View.VISIBLE
                    return@obtenerProductos
                }

                binding.textEstadoProductos.visibility = View.GONE

                productos.forEach { producto ->
                    val tarjeta = TextView(requireContext()).apply {
                        text = "${producto.Nombre}\n" +
                                "Precio: $${"%.2f".format(producto.Precio)} por ${producto.Unidad}\n" +
                                "Existencia: ${producto.Existencia}"
                        textSize = 15f
                        setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.black
                            )
                        )
                        setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.white
                            )
                        )
                        setPadding(32, 28, 32, 28)
                        gravity = Gravity.START
                    }

                    val parametros = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 16
                    }

                    binding.contenedorProductos.addView(tarjeta, parametros)
                }
            },
            alError = { error ->
                if (!isAdded || _binding == null) return@obtenerProductos

                binding.textEstadoProductos.visibility = View.VISIBLE
                binding.textEstadoProductos.text =
                    "Error Firebase: ${error.message}"
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
