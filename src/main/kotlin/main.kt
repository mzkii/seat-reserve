import java.security.SecureRandom

fun main(args: Array<String>) {
  createBlocks()
  val secureRandom = SecureRandom()
  val results = mutableListOf<LotteryResult>()
  repeat(10000) {
    val count = secureRandom.nextInt(1, 10)
    print("$count 人で抽選中 (rate=$rate)... ")
    val result = getLotteryResult(count, results)
    results.add(result)
    when (result) {
      is LotteryResult.Success -> {
        println("LotteryResult.Success: seatBlockName = $result")
      }

      is LotteryResult.Failed.CapacityOver -> {
        println("LotteryResult.Failed.CapacityOver")
      }

      is LotteryResult.Failed.Lose -> {
        println("LotteryResult.Failed.Lose")
      }
    }
  }
  printBlocks()
  printResults(results)
}
