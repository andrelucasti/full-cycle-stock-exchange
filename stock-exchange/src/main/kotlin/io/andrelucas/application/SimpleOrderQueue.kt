package io.andrelucas.application

import io.andrelucas.business.Order
import io.andrelucas.business.OrderQueue
import io.andrelucas.business.OrderType

class SimpleOrderQueue : OrderQueue {

    private val orders = mutableListOf<Order>()

    override fun push(order: Order) {
        orders.add(order)
    }

    override fun pop(): Order {
        if (orders.isNotEmpty()) {

            orders.removeFirst().let {
                return it
            }
        }
       return null!!
    }

    override fun size() = orders.size


    override fun match(inputOrder: Order): Boolean {
        return when (inputOrder.orderType) {
            OrderType.BUY -> orders.first().price >= inputOrder.price
            OrderType.SELL -> orders.first().price <= inputOrder.price
        }
    }
}