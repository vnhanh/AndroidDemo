
data class Flower(
    val id: String,
    val name: String,
    val categories: List<String>,
    val originPrice: Double,
    val discount: Double,
    val price: Double,
    val couponCodes: List<String>,
    val description: String,
    val imageUrls: List<String>,
    val tag: FlowerTag,
    val saleNumber: Long,
    val reviewNumber: Long,
    val likeNumber: Long,
    val rating: Double,
    val location: String,
)

data class FlowerTag(
    val type: Int,
    val value: String,
)

enum class FlowerTagValue(val value: Int) {
    NORMAL(0),
    NEW(1),
    BEST_SALE(2),
    BEST_PRICE(3),
}

fun Int.toFlowerTagValue(): FlowerTagValue =
    FlowerTagValue.values().firstOrNull { it.value == this } ?: FlowerTagValue.NORMAL
