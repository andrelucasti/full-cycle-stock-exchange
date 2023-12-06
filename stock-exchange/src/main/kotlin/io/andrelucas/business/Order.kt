package io.andrelucas.business

import java.util.UUID

data class Order(val id: UUID,
                 val investor: Investor,
                 val asset: Asset,
                 val share: Int,
                 val pendingShare: Int,
                 val price: Double,
                 val status: OrderStatus,
                 val orderType: OrderType,
                 var transactions: List<Transaction>) {

    companion object {
        fun create(investor: Investor,
                   asset: Asset,
                   share: Int,
                   price: Double,
                   orderType: OrderType ): Order {

            return Order(UUID.randomUUID(), investor, asset, share, share, price, OrderStatus.OPEN, orderType, emptyList())
        }
    }
}

enum class OrderType {
    BUY, SELL
}

enum class OrderStatus {
    OPEN,
    EXECUTED,
    PARTIALLY_EXECUTED,
    CANCELED
}