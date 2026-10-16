import java.io.File
import java.time.LocalDate

data class Expense(val date: String, val amount: Double, val category: String, val description: String)
val store = File("expenses.tsv")

fun load(): MutableList<Expense> {
    if (!store.exists()) return mutableListOf()
    return store.readLines().filter { it.isNotBlank() }.mapNotNull { line ->
        val p = line.split("\t", limit = 4)
        if (p.size == 4) Expense(p[0], p[1].toDoubleOrNull() ?: return@mapNotNull null, p[2], p[3]) else null
    }.toMutableList()
}
fun save(items: List<Expense>) = store.writeText(items.joinToString("\n") { it.date + "\t" + it.amount + "\t" + it.category + "\t" + it.description })
fun help() = println("Commands: add <amount> <category> <description...> | list [category] | delete <number> | summary [YYYY-MM] | total | average | categories | top-category | largest | stats | today | budget <amount> [YYYY-MM] | help")

fun main(args: Array<String>) {
    val items = load()
    when (args.firstOrNull() ?: "help") {
        "add" -> {
            if (args.size < 4) return help()
            val amount = args[1].toDoubleOrNull() ?: return println("Invalid amount.")
            if (!amount.isFinite() || amount <= 0) return println("Amount must be greater than zero.")
            val description = args.drop(3).joinToString(" ").trim()
            if (args[2].isBlank() || description.isEmpty()) return println("Category and description are required.")
            val e = Expense(LocalDate.now().toString(), amount, args[2], description)
            items += e; save(items); println("Saved: " + e.description + " - " + "%.2f".format(e.amount))
        }
        "list" -> {
            val category = args.getOrNull(1)
            val visible = if (category == null) items else items.filter { it.category.equals(category, ignoreCase = true) }
            if (visible.isEmpty()) println("No entries found.") else visible.forEach { println(it.date + " | " + it.category + " | " + "%.2f".format(it.amount) + " | " + it.description) }
        }
        "delete" -> {
            val index = args.getOrNull(1)?.toIntOrNull()?.minus(1) ?: return println("Use: delete <number>")
            if (index !in items.indices) return println("Expense not found.")
            val removed = items.removeAt(index)
            save(items)
            println("Deleted: " + removed.description)
        }
        "today" -> {
            val today = LocalDate.now().toString()
            val todays = items.filter { it.date == today }
            if (todays.isEmpty()) println("No expenses today.") else todays.forEach { println(it.date + " | " + it.category + " | " + "%.2f".format(it.amount) + " | " + it.description) }
            println("Today total: " + "%.2f".format(todays.sumOf { it.amount }))
        }
        "budget" -> {
            val limit = args.getOrNull(1)?.toDoubleOrNull()
            if (limit == null || !limit.isFinite() || limit < 0) {
                println("Use: budget <non-negative amount> [YYYY-MM]")
            } else {
                val month = args.getOrNull(2) ?: LocalDate.now().toString().take(7)
                if (!month.matches(Regex("\\d{4}-\\d{2}"))) {
                    println("Month must use YYYY-MM format.")
                } else {
                    val spent = items.filter { it.date.startsWith(month) }.sumOf { it.amount }
                    println("Budget for $month: " + "%.2f".format(limit) + " | Spent: " + "%.2f".format(spent) + " | Remaining: " + "%.2f".format(limit - spent))
                }
            }
        }
        "total" -> println("All-time total: " + "%.2f".format(items.sumOf { it.amount }))
        "average" -> println("Average expense: " + "%.2f".format(if (items.isEmpty()) 0.0 else items.map { it.amount }.average()))
        "categories" -> items.groupBy { it.category.lowercase() }.toSortedMap().forEach { (category, values) -> println(category + ": " + "%.2f".format(values.sumOf { it.amount })) }
        "top-category" -> {
            val top = items.groupBy { it.category }
                .maxByOrNull { (_, values) -> values.sumOf { it.amount } }
            if (top == null) println("No expenses yet.")
            else println("Top category: " + top.key + " | " + "%.2f".format(top.value.sumOf { it.amount }))
        }
        "stats" -> {
            val categories = items.map { it.category.lowercase() }.distinct().size
            println("Entries: " + items.size + " | Categories: " + categories + " | Total: " + "%.2f".format(items.sumOf { it.amount }))
        }
        "largest" -> {
            val largest = items.maxByOrNull { it.amount }
            if (largest == null) println("No expenses yet.") else println("Largest: " + "%.2f".format(largest.amount) + " | " + largest.category + " | " + largest.description)
        }
        "summary" -> {
            val month = args.getOrNull(1) ?: LocalDate.now().toString().take(7)
            val filtered = items.filter { it.date.startsWith(month) }
            println("Summary for " + month)
            filtered.groupBy { it.category }.toSortedMap().forEach { (cat, values) -> println(cat + ": " + "%.2f".format(values.sumOf { it.amount })) }
            println("Total: " + "%.2f".format(filtered.sumOf { it.amount }))
        }
        else -> help()
    }
}