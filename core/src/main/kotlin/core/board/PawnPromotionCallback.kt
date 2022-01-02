package core.board

fun interface PawnPromotionCallback {

    fun getPawnPromotionType(): PawnPromotionType
}