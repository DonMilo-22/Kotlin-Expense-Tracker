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
fun help() = println("Commands: add <amount> <category> <description...> | list | delete <number> | summary [YYYY-MM] | help")

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
        "list" -> if (items.isEmpty()) println("No entries yet.") else items.forEach { println(it.date + " | " + it.category + " | " + "%.2f".format(it.amount) + " | " + it.description) }
        "delete" -> {
            val index = args.getOrNull(1)?.toIntOrNull()?.minus(1) ?: return println("Use: delete <number>")
            if (index !in items.indices) return println("Expense not found.")
            val removed = items.removeAt(index)
            save(items)
            println("Deleted: " + removed.description)
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