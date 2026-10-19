import java.io.File
import java.math.BigDecimal

if (args.isEmpty()) {
    System.err.println("Usage: kotlinc -script tools/budget-check.kts -- <monthly-budget> [expenses.tsv]")
    exitProcess(2)
}
val budget = args[0].toBigDecimalOrNull()
if (budget == null || budget < BigDecimal.ZERO) {
    System.err.println("Budget must be a valid non-negative amount.")
    exitProcess(2)
}
val file = File(args.getOrNull(1) ?: "expenses.tsv")
if (!file.isFile) {
    System.err.println("Expense file not found: ${file.path}")
    exitProcess(1)
}
var total = BigDecimal.ZERO
var count = 0
file.forEachLine { line ->
    val columns = line.split('\t')
    if (line.isNotBlank() && columns.size >= 2) {
        val amount = columns[1].toBigDecimalOrNull()
        if (amount != null) {
            total = total.add(amount)
            count++
        }
    }
}
val remaining = budget.subtract(total)
println("Budget: $budget")
println("Expenses counted: $count")
println("Total spent: $total")
println("Remaining: $remaining")
if (remaining < BigDecimal.ZERO) println("Warning: budget exceeded by ${remaining.abs()}.")
