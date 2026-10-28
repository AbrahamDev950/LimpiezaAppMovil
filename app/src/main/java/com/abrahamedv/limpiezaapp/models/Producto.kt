
package com.abrahamedv.limpiezaapp.models

data class Producto(
    val id: String = "",
    val Nombre: String = "",
    val Precio: Double = 0.0,
    val Existencia: Double = 0.0,
    val Unidad: String = "Litro",
    val Activo: Boolean = true
)
