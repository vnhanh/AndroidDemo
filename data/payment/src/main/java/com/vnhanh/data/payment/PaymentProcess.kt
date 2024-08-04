package com.vnhanh.data.payment


/**
 * The sealed class is represented for processing of the BillingResult object
 * and the Purchase object by the a payment processor class.
 */
sealed class PaymentProcess {
    /**
     * The state is used to indicate the processor is processing the payment. E.g. consuming a product or acknowledging a subscription.
     */
    data object Processing : PaymentProcess()

    /**
     * The state is used to indicate the processor detects a pending purchase data, need the user to confirm it.
     */
    data class ConsumeConfirmation(
        val purchase: PurchaseResult,
        val pendingData: PendingSingleProgramData,
    ) : PaymentProcess()

    /**
     * The data class wraps a consumed result which might be a success payment, a failed payment, or a cancelled payment.
     * This consumed result is delivered from a Billing Client processor such as BillingHelper.
     */
    data class ConsumedResult(val result: ProductConsumingState) : PaymentProcess()

    /**
     * The state is used to indicate the processor did not consume any products, normally some parameter is invalid.
     */
    data object NoConsuming : PaymentProcess()

    /**
     * The state is used to indicate the processor did not perform any operations ever,
     * and the consumer (collector) normally do nothing.
     */
    data object Void : PaymentProcess()
}
