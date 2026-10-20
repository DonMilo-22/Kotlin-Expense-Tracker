import java.io.File
import java.math.BigDecimal

if (args.isEmpty()) {
    System.err.println("Usage: kotlinc -script tools/category-summary.kts -- <expenses.tsv>")
    kotlin.system.exitProcess(2)
}
val file = File(args[0])
if (!file.isFile) {
    System.err.println("Expense file not found: ${file.path}")
    kotlin.system.exitProcess(1)
}
val totals = sortedMapOf<String, BigDecimal>()
file.forEachLine { line ->
    val columns = line.split('\t')
    if (columns.size >= 3) {
        val category = columns[2].trim().ifEmpty { "Uncategorized" }
        val amount = columns[1].toBigDecimalOrNull()
        if (amount != null) totals[category] = (totals[category] ?: BigDecimal.ZERO).add(amount)
    }
}
if (totals.isEmpty()) println("No categorized expense rows found.")
else totals.forEach { (category, amount) -> println("$category\t$amount") }
