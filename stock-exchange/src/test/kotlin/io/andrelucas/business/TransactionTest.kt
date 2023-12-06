package io.andrelucas.business

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TransactionTest : FunSpec({

    test("should create a transaction") {
        val asset = Asset.create("PETR4", 100)

        val andreInvestor = Investor.create("Andre")
        val buyingOrder = Order.create(andreInvestor, asset, 1, 10.0, OrderType.BUY)

        val lucasInvestor = Investor.create("Lucas")
        val sellingOrder = Order.create(lucasInvestor, asset, 1, 10.0, OrderType.SELL)

        val transaction = Transaction.create(buyingOrder, sellingOrder, 1, 10.0)

        transaction.buyingOrder shouldBe buyingOrder
        transaction.sellingOrder shouldBe sellingOrder
        transaction.share shouldBe 1
        transaction.price shouldBe 10.0
        transaction.total shouldBe 10.0
    }
})
