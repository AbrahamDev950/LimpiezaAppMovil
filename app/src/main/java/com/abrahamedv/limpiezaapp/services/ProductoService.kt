
package com.abrahamedv.limpiezaapp.services
import com.abrahamedv.limpiezaapp.models.Producto

import com.google.firebase.firestore.FirebaseFirestore

class ProductoService {

    private val db = FirebaseFirestore.getInstance()

    private val coleccion = db.collection("producto")




    fun obtenerProductos(
        alCompletar: (List<Producto>) -> Unit,
        alError: (Exception) -> Unit
    ) {
        coleccion.get()
            .addOnSuccessListener { resultado ->
                val productos = resultado.documents.mapNotNull { documento ->
                    documento.toObject(Producto::class.java)?.copy(
                        id = documento.id
                    )
                }

                alCompletar(productos)
            }
            .addOnFailureListener { error ->
                alError(error)
            }
    }

    fun agregarProducto(
        producto: Producto,
        alCompletar: (String) -> Unit,
        alError: (Exception) -> Unit
    ) {
        val datos = hashMapOf(
            "nombre" to producto.nombre,
            "precio" to producto.precio,
            "existencia" to producto.existencia,
            "unidad" to producto.unidad,
            "activo" to producto.activo
        )

        coleccion.add(datos)
            .addOnSuccessListener { documento ->
                alCompletar(documento.id)
            }
            .addOnFailureListener { error ->
                alError(error)
            }
    }
}
