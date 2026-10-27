
package com.abrahamedv.limpiezaapp.models

data class Producto(
    val id: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val existencia: Int = 0,
    val unidad: String = "Litro",
    val activo: Boolean = true
)
