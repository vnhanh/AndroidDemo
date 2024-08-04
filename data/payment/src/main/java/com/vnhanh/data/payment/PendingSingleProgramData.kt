package com.vnhanh.data.payment

import kotlinx.serialization.Serializable

@Serializable
data class PendingSingleProgramData(
    val transactionId: String,
    val orderId: String?,
    val packageName: String?,
    val originPrice: String,
    val price: String,
    val currency: String,
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is PendingSingleProgramData) return false

        return this.transactionId == other.transactionId
                && this.orderId == other.orderId
                && this.originPrice == other.originPrice
                && this.price == other.price
                && this.currency == other.currency
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
