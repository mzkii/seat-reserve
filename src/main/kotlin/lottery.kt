sealed class LotteryResult {
  abstract val count: Int

  class Success(
    val block: Block,
    override val count: Int
  ) : LotteryResult() {
    override fun toString(): String {
      val seats = block.seats.map { seat -> "${block.name}-${seat.col}-${seat.row}" }
      return seats.joinToString(", ")
    }
  }

  sealed class Failed : LotteryResult() {
    class CapacityOver(
      override val count: Int
    ) : Failed()

    class Lose(
      override val count: Int
    ) : Failed()
  }
}

private fun lottery(successRate: Double): Boolean {
  return Math.random() < successRate
}

private fun List<LotteryResult>.currentSuccessRate(): Double {
  return filterIsInstance<LotteryResult.Success>().size / size.toDouble()
}

var rate: Double = 0.04
  private set

private fun getRate(results: List<LotteryResult>): Double {
  // TODO(mzkii): 最後まで当たりが残るように、いい感じに rate を動的に調整する
  return rate
}

fun getLotteryResult(count: Int, results: List<LotteryResult>): LotteryResult {
  val isSuccess = lottery(getRate(results))
  if (!isSuccess) {
    return LotteryResult.Failed.Lose(count)
  }
  val result = reserve(count)
  if (result.seats.isEmpty()) {
    return LotteryResult.Failed.CapacityOver(count)
  }
  return LotteryResult.Success(result, count)
}
