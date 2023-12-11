package io.andrelucas.business

import io.andrelucas.application.SimpleOrderQueue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class Book(val orders: List<Order>,
                var transactions: List<Transaction>,
                val ordersChannelInput: Channel<Order>,
                val ordersChannelOutput: Channel<Order>){

    companion object {
        fun create(ordersChannelInput: Channel<Order>,
                   ordersChannelOutput: Channel<Order>): Book {

            return Book(emptyList(), emptyList(), ordersChannelInput, ordersChannelOutput)
        }
    }

    suspend fun trade() = coroutineScope {
        val buyingOrders = SimpleOrderQueue()
        val sellingOrders = SimpleOrderQueue()

        launch {
            for(inputOrder in ordersChannelInput) {
                if(inputOrder.orderType == OrderType.BUY){
                    buyingOrders.push(inputOrder)
                    if(sellingOrders.size() > 0 && sellingOrders.match(inputOrder)){
                        val sellOrder = sellingOrders.pop()
                        if(sellOrder.pendingShare > 0 ){
                            val transaction = Transaction.create(inputOrder, sellOrder, inputOrder.pendingShare, sellOrder.price)
                            addTransaction(transaction)
                            inputOrder.transactions.plus(transaction)
                            sellOrder.transactions.plus(transaction)

                           // ordersChannelOutput.send(inputOrder)
                            //ordersChannelOutput.send(sellOrder)

                            if (sellOrder.pendingShare > 0) {
                                sellingOrders.push(sellOrder)
                            }
                        }
                    }
                }

                if (inputOrder.orderType == OrderType.SELL) {
                    sellingOrders.push(inputOrder)

                    if (buyingOrders.size() > 0 && buyingOrders.match(inputOrder)) {
                        val buyOrder = buyingOrders.pop()
                        if (buyOrder.pendingShare > 0) {
                            val transaction = Transaction.create(buyOrder, inputOrder, inputOrder.pendingShare, buyOrder.price)
                            addTransaction(transaction)
                            inputOrder.transactions.plus(transaction)
                            buyOrder.transactions.plus(transaction)

                            //ordersChannelOutput.send(buyOrder)
                            //ordersChannelOutput.send(inputOrder)

                            if (buyOrder.pendingShare > 0) {
                                buyingOrders.push(buyOrder)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun addTransaction(transaction: Transaction) {
        val buyingOrdersPendingShare = transaction.buyingOrder.pendingShare //10
        val sellingOrdersPendingShare = transaction.sellingOrder.pendingShare //5
        val minus = if (buyingOrdersPendingShare > sellingOrdersPendingShare) sellingOrdersPendingShare else buyingOrdersPendingShare

        transaction.buyingOrder.investor.updateAssetShare(transaction.buyingOrder.asset.id, minus)
        transaction.buyingOrder.pendingShare = buyingOrdersPendingShare - minus

        transaction.sellingOrder.investor.updateAssetShare(transaction.sellingOrder.asset.id, - minus)
        transaction.sellingOrder.pendingShare = sellingOrdersPendingShare - minus

        transaction.total = transaction.share * transaction.buyingOrder.price

        if (transaction.buyingOrder.pendingShare == 0) {
            transaction.buyingOrder.status = OrderStatus.EXECUTED
        } else {
            transaction.buyingOrder.status = OrderStatus.PARTIALLY_EXECUTED
        }

        if (transaction.sellingOrder.pendingShare == 0) {
            transaction.sellingOrder.status = OrderStatus.EXECUTED
        } else {
            transaction.sellingOrder.status = OrderStatus.PARTIALLY_EXECUTED
        }

        transactions = transactions.plus(transaction)
    }
}