package io.andrelucas.business

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class BookTest : FunSpec({

    test("should create a book") {
        val orderChannelInput = Channel<Order>()
        val orderChannelOutput = Channel<Order>()
        val book = Book.create(orderChannelInput, orderChannelOutput)

        book.orders shouldBe emptyList()
        book.transactions shouldBe emptyList()
    }

    test("should create a transaction when happens a trade"){
        val petra = Asset.create("PETR4", 200)

        val andreInvestor = Investor.create("Andre")
        andreInvestor.addAsset(petra.id, 100)
        val andreOrder = Order.create(andreInvestor, petra, 10, 10.0, OrderType.BUY)

        val lucasInvestor = Investor.create("Lucas")
        val lucasAsset = Asset.create("PETR4", 100)
        lucasInvestor.addAsset(lucasAsset.id, 100)
        val lucasOrder = Order.create(lucasInvestor, lucasAsset, 10, 10.0, OrderType.SELL)

        val orderChannelInput = Channel<Order>(2)
        val orderChannelOutput = Channel<Order>()

        launch {
            orderChannelInput.send(andreOrder)
            orderChannelInput.send(lucasOrder)
            orderChannelInput.close()
        }

        val book = Book.create(orderChannelInput, orderChannelOutput)
        book.trade()

        book.transactions.size shouldBe 1
        book.transactions[0].buyingOrder.status shouldBe OrderStatus.EXECUTED
        book.transactions[0].sellingOrder.status shouldBe OrderStatus.EXECUTED
        book.transactions[0].share shouldBe 10
        book.transactions[0].price shouldBe 10.0

        andreInvestor.investorAssetPosition[0].share shouldBe 110
        lucasInvestor.investorAssetPosition[0].share shouldBe 90

        //book.orders.size shouldBe 2
    }
})
