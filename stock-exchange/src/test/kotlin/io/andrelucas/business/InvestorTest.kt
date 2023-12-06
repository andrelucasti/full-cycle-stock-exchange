package io.andrelucas.business

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.*

class InvestorTest : FunSpec({

    test("should create an investor") {
        val investor = Investor.create("Andre")
        investor.name shouldBe "Andre"
    }

    test("should add an asset to investor") {
        val investor = Investor.create("Andre")
        investor.addAsset(UUID.randomUUID(), 10)
        investor.investorAssetPosition.count() shouldBe 1
    }

    test("should add two assets to investor") {
        val investor = Investor.create("Andre")
        investor.addAsset(UUID.randomUUID(), 10)
        investor.addAsset(UUID.randomUUID(), 20)
        investor.investorAssetPosition.size shouldBe 2
    }

    test("should update asset share"){
        val investor = Investor.create("Andre")
        val assetId = UUID.randomUUID()
        investor.addAsset(assetId, 10)
        investor.updateAssetShare(assetId, 20)

        investor.investorAssetPosition[0].share shouldBe 20
    }

    test("should create asset when try to update asset share and asset does not exist"){
        val investor = Investor.create("Andre")
        val assetId = UUID.randomUUID()
        investor.updateAssetShare(assetId, 20)

        investor.investorAssetPosition[0].share shouldBe 20
    }
})
