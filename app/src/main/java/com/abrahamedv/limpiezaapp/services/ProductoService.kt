
package com.abrahamedv.limpiezaapp.services
import com.abrahamedv.limpiezaapp.models.Producto
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductoService {

    private val db = FirebaseFirestore.getInstance()

    private val coleccion = db.collection("producto")




    fun obtenerProductos(
        alCompletar: (List<Producto>) -> Unit,
        alError: (Exception) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()

        Log.d(
            "EcoCleanFirebase",
            "Firestore: app=${db.app.name}, proyecto=${db.app.options.projectId}"
        )

        Log.d(
            "EcoCleanFirebase",
            "Auth: app=${auth.app.name}, proyecto=${auth.app.options.projectId}"
        )

        Log.d(
            "EcoCleanFirebase",
            "UID actual: ${auth.currentUser?.uid}"
        )
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
                Log.e(
                    "EcoCleanFirebase",
                    "Error al consultar productos",
                    error
                )

                alError(error)
            }
    }

    fun agregarProducto(
        producto: Producto,
        alCompletar: (String) -> Unit,
        alError: (Exception) -> Unit
    ) {
        val datos = hashMapOf(
            "Nombre" to producto.Nombre,
            "Precio" to producto.Precio,
            "Existencia" to producto.Existencia,
            "Unidad" to producto.Unidad,
            "Activo" to producto.Activo
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
