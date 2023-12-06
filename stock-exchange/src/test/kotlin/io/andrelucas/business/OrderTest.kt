package io.andrelucas.business

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OrderTest : FunSpec({

    test("should create an order") {
        val investor = Investor.create("Andre")
        val order = Order.create(investor, Asset.create("PETR4", 100), 10, 10.0, OrderType.BUY)

        order.investor.name shouldBe "Andre"
        order.asset.name shouldBe "PETR4"
        order.share shouldBe 10
        order.pendingShare shouldBe 10
        order.pendingShare shouldBe order.share
        order.price shouldBe 10.0
        order.status shouldBe OrderStatus.OPEN
        order.orderType shouldBe OrderType.BUY
        order.transactions shouldBe emptyList()
    }

})
