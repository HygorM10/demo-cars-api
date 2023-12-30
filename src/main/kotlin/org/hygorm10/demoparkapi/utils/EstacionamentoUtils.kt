package org.hygorm10.demoparkapi.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class EstacionamentoUtils {

    companion object {

        private const val PRIMEIROS_15_MINUTES = 5.00
        private const val PRIMEIROS_60_MINUTES = 9.25
        private const val ADICIONAL_15_MINUTES = 1.75
        private const val DESCONTO_PERCENTUAL = 0.30

        fun calcularCusto(entrada: LocalDateTime, saida: LocalDateTime?): BigDecimal {
            val minutes = entrada.until(saida, ChronoUnit.MINUTES)
            var total = 0.0

            if (minutes <= 15) {
                total = PRIMEIROS_15_MINUTES
            } else if (minutes <= 60) {
                total = PRIMEIROS_60_MINUTES
            } else {
                val addicionalMinutes = minutes - 60
                val totalParts = (addicionalMinutes.toDouble() / 15)
                total += if (totalParts > totalParts.toInt()) { // 4.66 > 4
                    PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * (totalParts.toInt() + 1))
                } else { // 4.0
                    PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * totalParts.toInt())
                }
            }

            return BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN)
        }

        fun calcularDesconto(custo: BigDecimal, numeroDeVezes: Long): BigDecimal {
            val desconto = if (((numeroDeVezes > 0) && (numeroDeVezes % 10 == 0L))
            ) custo.multiply(BigDecimal(DESCONTO_PERCENTUAL))
            else BigDecimal(0)
            return desconto.setScale(2, RoundingMode.HALF_EVEN)
        }

        fun gerarRecibo(): String {
            val date = LocalDateTime.now()
            val recibo: String = date.toString().substring(0, 19)
            return recibo
                .replace("-", "")
                .replace(":", "")
                .replace("T", "-")
        }
    }

}