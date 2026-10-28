
package com.abrahamedv.limpiezaapp.services

import com.google.firebase.auth.FirebaseAuth

class AuthService {

    private val auth = FirebaseAuth.getInstance()

    fun autenticarAnonimamente(
        alCompletar: () -> Unit,
        alError: (Exception) -> Unit
    ) {
        // Si ya existe una sesión, la reutilizamos.
        if (auth.currentUser != null) {
            alCompletar()
            return
        }

        // Si no existe, creamos una sesión anónima.
        auth.signInAnonymously()
            .addOnSuccessListener {
                alCompletar()
            }
            .addOnFailureListener { error ->
                alError(error)
            }
    }
}
