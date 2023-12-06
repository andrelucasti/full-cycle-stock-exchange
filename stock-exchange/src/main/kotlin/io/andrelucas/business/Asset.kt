package io.andrelucas.business

import java.util.UUID

data class Asset(val id: UUID,
                 val name: String,
                 val marketVolume: Int) {

    companion object {
        fun create(name: String, marketVolume: Int): Asset {
            return Asset(UUID.randomUUID(), name, marketVolume)
        }
    }
}