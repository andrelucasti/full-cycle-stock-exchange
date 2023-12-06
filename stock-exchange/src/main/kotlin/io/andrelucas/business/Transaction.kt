package io.andrelucas.business

import java.time.LocalDateTime
import java.util.UUID

data class Transaction(val id: UUID,
                       val buyingOrder: Order,
                       val sellingOrder: Order,
                       val share: Int,
                       val price: Double,
                       val total : Double,
                       val dateTime: LocalDateTime) {
    companion object {

        fun create(buyingOrder: Order,
                   sellingOrder: Order,
                   share: Int,
                   price: Double): Transaction {

            val total = share * price
            return Transaction(UUID.randomUUID(), buyingOrder, sellingOrder, share, price, total, LocalDateTime.now())
        }
    }
}