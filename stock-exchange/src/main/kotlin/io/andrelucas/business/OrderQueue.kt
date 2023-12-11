package io.andrelucas.business

interface OrderQueue {
    fun push(order: Order)
    fun pop(): Order
    fun size(): Int
    fun match(inputOrder: Order): Boolean
}