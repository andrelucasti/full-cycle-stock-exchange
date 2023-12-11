package io.andrelucas.business

import java.util.UUID

data class Investor(val id: UUID,
                    val name: String,
                    var investorAssetPosition: List<InvestorAssetPosition>){

    companion object {
        fun create(name: String): Investor {
            return Investor(UUID.randomUUID(), name, emptyList())
        }
    }

    fun addAsset(assetId: UUID, share: Int) {
        val assetPosition = InvestorAssetPosition(assetId, share)
        investorAssetPosition = investorAssetPosition.plus(assetPosition)
    }

    fun updateAssetShare(assetId: UUID, share: Int) {
        val assetPosition = investorAssetPosition.find { it.assetId == assetId }
        assetPosition?.let {
            val updatedAssetPosition = it.copy(share = share + it.share)
            investorAssetPosition = investorAssetPosition.minus(it).plus(updatedAssetPosition)
        } ?: addAsset(assetId, share)
    }
}

data class InvestorAssetPosition(val assetId: UUID, val share: Int)