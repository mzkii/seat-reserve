import java.security.SecureRandom

fun main(args: Array<String>) {
  createBlocks()
  val secureRandom = SecureRandom()
  val results = mutableListOf<LotteryResult>()
  repeat(9400) {
    val count = secureRandom.nextInt(1, 5)
    print("$count 人で抽選中 (rate=$rate)... ")
    val result = getLotteryResult(count, results)
    results.add(result)
    when (result) {
      is LotteryResult.Success -> {
        println("当たり！ 座席は $result です。")
      }
      is LotteryResult.Failed.CapacityOver -> {
        println("座席が満席のためハズレです。")
      }
      is LotteryResult.Failed.Lose -> {
        println("ハズレです。")
      }
    }
  }
  printBlocks()
  printResults(results)
}
