import kotlin.math.roundToInt

data class Seat(var isReserved: Boolean, val col: Int, val row: Int) {
  override fun toString(): String {
    return if (isReserved) "■" else "□"
  }
}

data class Block(val name: String, val seats: List<Seat>) {
  fun reservableSeats(count: Int): Block {
    val cols = seats.map { it.col }.distinct().toMutableList()
    while (cols.isNotEmpty()) {
      val col = cols.random()
      cols.remove(col)
      val isReservedSeats = seats.filter { it.col == col }
      val isReservedString = isReservedSeats.joinToString("") { if (it.isReserved) "1" else "0" }
      val requestReserveString = List(count) { "0" }.joinToString("")
      val index = if (col % 2 == 0) {
        isReservedString.indexOf(requestReserveString)
      } else {
        isReservedString.lastIndexOf(requestReserveString)
      }
      if (index >= 0) {
        val subList = isReservedSeats.subList(index, index + count)
        for (seat in subList) {
          seat.isReserved = true
        }
        return copy(seats = subList)
      }
    }
    return copy(seats = emptyList())
  }

  override fun toString(): String {
    println("$name ブロック:")
    val maxCol = seats.maxOf { it.col }
    val minCol = seats.minOf { it.col }
    for (col in minCol..maxCol) {
      val rows = seats
        .filter { it.col == col }
        .sortedBy { it.col }
        .joinToString(" ")
      println(rows)
    }
    return super.toString()
  }
}

private val reservableBlocks = mutableListOf<Block>()

fun printBlocks() {
  val blocks = reservableBlocks.sortedBy { it.name }
  for (block in blocks) {
    println(block)
  }
}

fun printResults(resultList: List<LotteryResult>) {
  val resultMap = mutableMapOf<Int, List<LotteryResult>>()
  for (result in resultList) {
    resultMap[result.count] = resultList.filter { it.count == result.count }
  }
  for ((count, results) in resultMap.toSortedMap()) {
    val totalTryCount = results.size
    val totalSuccessCount = results.filterIsInstance<LotteryResult.Success>().size
    val rate = totalSuccessCount.toDouble() / totalTryCount * 100
    println("$count 人の当選確率: $totalSuccessCount/$totalTryCount = ${(rate * 1000.0).roundToInt() / 1000.0}%")
  }
}

fun createBlocks() {
  for (blockName in 'A'..'I') {
    val seats = mutableListOf<Seat>()
    for (col in 1..11) {
      for (row in 1..16) {
        seats.add(Seat(isReserved = false, col = col, row = row))
      }
    }
    val block = Block(blockName.toString(), seats)
    reservableBlocks.add(block)
  }
}

fun reserve(count: Int): Block {
  val blocks = reservableBlocks.shuffled()
  for (block in blocks) {
    val result = block.reservableSeats(count)
    if (result.seats.isNotEmpty()) return result
  }
  return Block(name = "", seats = emptyList())
}
