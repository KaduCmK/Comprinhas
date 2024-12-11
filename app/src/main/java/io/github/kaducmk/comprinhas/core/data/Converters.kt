package io.github.kaducmk.comprinhas.core.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    companion object {
        fun isoInstantString_to_LocalDateTime(isoInstant: String): String {
            val isoDateTime = LocalDateTime.parse(
                isoInstant,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            )

            return isoDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }

        fun isoInstantString_to_ExtendedDate(isoInstant: String): String {
            val isoDateTime = LocalDateTime.parse(
                isoInstant,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            )

            return isoDateTime.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"))
        }

        fun parseCnpj(cnpj: String): String {
            return cnpj.substring(0, 2) + "." +
                    cnpj.substring(2, 5) + "." +
                    cnpj.substring(5, 8) + "/" +
                    cnpj.substring(8, 12) + "-" +
                    cnpj.substring(12, 14)
        }

        fun parseUnit(qtd: Int, unit: String): Number {
            val weightUnits = listOf("KG", "ML")

            return if (unit.uppercase() in weightUnits) qtd / 1000f
            else qtd
        }
    }
}