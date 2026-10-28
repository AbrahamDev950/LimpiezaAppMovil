package com.abrahamedv.limpiezaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abrahamedv.limpiezaapp.databinding.FragmentRegistrarProductoBinding
import com.abrahamedv.limpiezaapp.models.Producto
import com.abrahamedv.limpiezaapp.services.ProductoService

class RegistrarProductoFragment : Fragment() {

    private var _binding: FragmentRegistrarProductoBinding? = null
    private val binding get() = _binding!!

    private val productoService = ProductoService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrarProductoBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGuardarProducto.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val nombre = binding.editNombre.text.toString().trim()
        val precioTexto = binding.editPrecio.text.toString().trim()
        val existenciaTexto = binding.editExistencia.text.toString().trim()
        val unidad = binding.editUnidad.text.toString().trim()

        if (
            nombre.isBlank() ||
            precioTexto.isBlank() ||
            existenciaTexto.isBlank() ||
            unidad.isBlank()
        ) {
            Toast.makeText(
                requireContext(),
                "Completa todos los campos.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val precio = precioTexto.toDoubleOrNull()
        val existencia = existenciaTexto.toDoubleOrNull()

        if (precio == null || precio <= 0) {
            binding.editPrecio.error = "Ingresa un precio válido."
            return
        }

        if (existencia == null || existencia < 0) {
            binding.editExistencia.error = "Ingresa una existencia válida."
            return
        }

        val producto = Producto(
            Nombre = nombre,
            Precio = precio,
            Existencia = existencia,
            Unidad = unidad,
            Activo = true
        )

        binding.buttonGuardarProducto.isEnabled = false

        productoService.agregarProducto(
            producto = producto,
            alCompletar = { id ->
                if (!isAdded || _binding == null) return@agregarProducto

                binding.buttonGuardarProducto.isEnabled = true

                Toast.makeText(
                    requireContext(),
                    "Producto registrado correctamente.",
                    Toast.LENGTH_LONG
                ).show()

                limpiarFormulario()
            },
            alError = { error ->
                if (!isAdded || _binding == null) return@agregarProducto

                binding.buttonGuardarProducto.isEnabled = true

                Toast.makeText(
                    requireContext(),
                    "Error al guardar: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    private fun limpiarFormulario() {
        binding.editNombre.text?.clear()
        binding.editPrecio.text?.clear()
        binding.editExistencia.text?.clear()
        binding.editUnidad.text?.clear()

        binding.editNombre.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
