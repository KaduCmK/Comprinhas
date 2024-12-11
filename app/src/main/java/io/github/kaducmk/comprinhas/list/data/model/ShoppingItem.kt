package io.github.kaducmk.comprinhas.list.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItem(
    val id: String? = null,
    val nome: String? = null,
    val adicionadoPor: Usuario? = null
)