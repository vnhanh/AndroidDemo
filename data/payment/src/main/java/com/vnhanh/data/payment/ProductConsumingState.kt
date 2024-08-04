package com.vnhanh.data.payment

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

/**
 * This sealed defines the possible states a purchase processor can have.
 */
sealed class ProductConsumingState {
    data object Loading : ProductConsumingState()

    data class Success(val purchase: Purchase) : ProductConsumingState()

    data class Pending(val purchase: Purchase) : ProductConsumingState()

    data object BillingClientDisconnected : ProductConsumingState()

    data object ItemAlreadyOwned : ProductConsumingState()

    data class Failure(
        val errorCode: BillingProcessCode = BillingProcessCode.ERROR_UNKNOWN,
        val message: String? = null
    ) : ProductConsumingState()
}

/**
 * Used for helping to determining the reason of the error if users raised
 */
enum class BillingProcessCode(val code: Int) {
    OK(BillingClient.BillingResponseCode.OK),
    ITEM_ALREADY_OWNED(BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED),
    USER_CANCELED(BillingClient.BillingResponseCode.USER_CANCELED),
    BILLING_UNAVAILABLE(BillingClient.BillingResponseCode.BILLING_UNAVAILABLE),
    ERROR(BillingClient.BillingResponseCode.ERROR),
    NETWORK_ERROR(BillingClient.BillingResponseCode.NETWORK_ERROR),
    DEVELOPER_ERROR(BillingClient.BillingResponseCode.DEVELOPER_ERROR),
    FEATURE_NOT_SUPPORTED(BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED),
    ITEM_UNAVAILABLE(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE),
    SERVICE_DISCONNECTED(BillingClient.BillingResponseCode.SERVICE_DISCONNECTED),
    SERVICE_UNAVAILABLE(BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE),

    // The App could not found the product id from the returned purchase object from the Google Play Billing
    NOT_FOUND_PURCHASE_ID(1000),
    // failed to consume product
    CONSUME_FAILURE(1001),
    PENDING_PURCHASE(1002),
    ERROR_UNKNOWN(1004),
    ERROR_NOT_FOUND_PURCHASE(1005);

    companion object {
        fun from(code: Int?) : BillingProcessCode {
            code ?: return ERROR_UNKNOWN
            return entries.firstOrNull { value -> value.code == code } ?: ERROR_UNKNOWN
        }
    }
}
