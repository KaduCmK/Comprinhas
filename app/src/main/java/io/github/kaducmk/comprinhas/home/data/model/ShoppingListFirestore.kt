package io.github.kaducmk.comprinhas.home.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario

data class ShoppingListFirestore(
    val nome: String? = null,
    val senha: String? = null,
    val criador: Usuario? = null,
    val imgUrl: String? = null
)
