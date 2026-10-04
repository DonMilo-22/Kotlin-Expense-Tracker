# 💸 Kotlin Expense Tracker

A simple terminal expense tracker built with Kotlin.

![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white)
![JVM](https://img.shields.io/badge/JVM-17%2B-E76F00?logo=openjdk&logoColor=white)

## What it does
Register everyday expenses, organize them by category, list your history and calculate monthly totals. Everything is stored locally.

## Features
- Add expenses with amount, category and description
- List saved entries
- View totals by category
- Filter summaries by month
- Local storage with no database

## Run
```bash
gradle run --args='add 149.90 food Dinner'
gradle run --args='list'
gradle run --args='summary'
```

## Commands
```text
add <amount> <category> <description...>
list
summary [YYYY-MM]
help
```

## Requirements
JDK 17+ and Gradle.

## Storage
The app creates `expenses.tsv` automatically.

## License
MIT.

## 🆕 Recent changes

### 2026-10-04

- New input validation rejects zero or negative amounts and empty expense descriptions.

### Previous update

- Added `delete <number>` so saved expenses can be removed directly from the CLI.
