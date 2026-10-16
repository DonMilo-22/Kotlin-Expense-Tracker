# Kotlin Expense Tracker

Registro local de gastos desde la terminal, desarrollado con Kotlin/JVM.

Permite agregar gastos, consultarlos por categoría y obtener resúmenes mensuales sin una base de datos.

## Funciones actuales

- Agregar, listar y eliminar gastos.
- Resumen mensual por categoría y total general.
- Promedio, gasto mayor y totales por categoría de todo el historial.
- Validación de importes positivos y finitos, categoría y descripción.
- Persistencia local en TSV.

## Requisitos

JDK 17 y Gradle compatible con Kotlin JVM 2.0.21, versión declarada en `build.gradle.kts`. El repositorio no incluye Gradle Wrapper; los comandos usan una instalación local de `gradle`.

## Instalación y uso

```bash
git clone https://github.com/DonMilo-22/Kotlin-Expense-Tracker.git
cd Kotlin-Expense-Tracker
gradle run --args='add 149.90 comida Cena'
gradle run --args='list'
gradle run --args='summary'
```

## Comandos

Ejecuta cada comando con `gradle run --args='<comando>'`.

| Comando | Ejemplo | Resultado |
|---|---|---|
| `add <importe> <categoría> <descripción...>` | `add 149.90 comida Cena` | Registra un gasto con fecha local de hoy. |
| `list [categoría]` | `list comida` | Lista todos o filtra sin distinguir mayúsculas. |
| `delete <número>` | `delete 1` | Borra la posición de base 1 en el historial completo. |
| `summary [YYYY-MM]` | `summary 2026-10` | Resume el mes indicado; sin argumento usa el actual. |
| `total` | `total` | Total de todo el historial. |
| `average` | `average` | Importe promedio; cero si no hay gastos. |
| `categories` | `categories` | Totales de todas las categorías, agrupando sin distinguir mayúsculas. |
| `largest` | `largest` | Gasto de mayor importe. |
| `help` | `help` | Muestra ayuda. |

## Almacenamiento y límites

`expenses.tsv` se crea en el directorio de trabajo del proceso. Al usar `gradle run` desde la raíz del proyecto se guarda ahí. Cada línea contiene fecha, importe, categoría y descripción separados por tabuladores.

`delete` usa la posición del archivo completo, no la posición de una lista filtrada; `list` no imprime números. Revisa el historial completo antes de borrar y conserva una copia si la necesitas. No hay recuperación, sincronización, selector de moneda ni edición de fecha. Los importes usan `Double`; es una práctica educativa, no un sistema contable.

## Comprobaciones

`gradle build` comprueba la compilación. `gradle run --args='help'` muestra la referencia disponible.

## Autor y licencia

Desarrollado por [DonMilo-22](https://github.com/DonMilo-22).

Licencia: MIT.

## Actualización 2026-10-12

- Added a `stats` command with entry count, category count and all-time total.

## Actualización adicional 2026-10-12

- Added the `today` command to show expenses recorded on the current date and their total.

- Added the `today` command to show only expenses recorded on the current date.

## Historial

Consulta [CHANGELOG.md](CHANGELOG.md) para ver las actualizaciones.

## Actualización 2026-10-14

- Added `budget <amount> [YYYY-MM]` to compare a monthly spending limit with recorded expenses.

## Actualización 2026-10-15

- Added `top-category` to show the category with the highest total spending.
