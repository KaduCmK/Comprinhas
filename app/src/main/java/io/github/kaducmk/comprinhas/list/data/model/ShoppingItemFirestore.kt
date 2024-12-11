package io.github.kaducmk.comprinhas.list.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario

data class ShoppingItemFirestore(
    val id: String? = null,
    val nome: String? = null,
    val adicionadoPor: Usuario? = null,
    val onCart: Boolean? = null
)
