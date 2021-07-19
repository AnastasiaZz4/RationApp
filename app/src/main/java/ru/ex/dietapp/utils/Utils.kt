package ru.ex.dietapp.utils

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {
        // форматирование миллисекунд в дату по формату
        fun getDate(milliSeconds: Long, dateFormat: String?): String? {
            val formatter = SimpleDateFormat(dateFormat)

            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        // создание и заполнение бд
        fun initDataBase(activity: Activity) {
            val db: SQLiteDatabase =
                activity.openOrCreateDatabase("app.db", MODE_PRIVATE, null)

            // создание таблиц
            db.execSQL("CREATE TABLE IF NOT EXISTS Disease (Name TEXT, MaxKcal INTEGER)")
            db.execSQL("CREATE TABLE IF NOT EXISTS Product (Name TEXT, Kcal INTEGER)")
            db.execSQL("CREATE TABLE IF NOT EXISTS DiseaseBadProduct (ID INTEGER PRIMARY KEY AUTOINCREMENT, Disease TEXT, Product TEXT)")
            db.execSQL("CREATE TABLE IF NOT EXISTS Dish (Name TEXT, Kcal INTEGER)")
            db.execSQL("CREATE TABLE IF NOT EXISTS DishProduct (ID INTEGER PRIMARY KEY AUTOINCREMENT, Dish TEXT, Product INTEGER)")

            // заполнение таблиц данными
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Сахар", 200))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Чипсы", 400))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Семечки", 300))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Молоко", 250))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Варенье", 240))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Консервы", 320))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Майонез", 120))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Сок", 80))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Компот", 150))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Сыр", 220))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Сало", 500))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Рыба", 250))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Пирожное", 430))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Творог", 210))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Яйца", 170))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Чай", 50))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Кисель", 220))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Пюре", 600))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Сухари", 20))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Банан", 400))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Манка", 200))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Яблоко", 60))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Груша", 40))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Ананас", 70))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Курица", 430))
            db.execSQL("INSERT INTO Product(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("Шоколад", 200))

            db.execSQL("INSERT INTO Disease(\"Name\", \"MaxKcal\") VALUES($1, $2)", arrayOf("Гастрит", 800))
            db.execSQL("INSERT INTO Disease(\"Name\", \"MaxKcal\") VALUES($1, $2)", arrayOf("Диабет", 700))

            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Сахар"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Чипсы"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Семечки"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Молоко"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Варенье"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Консервы"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Майонез"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Сок"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Диабет", "Компот"))

            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Гастрит", "Сыр"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Гастрит", "Сало"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Гастрит", "Рыба"))
            db.execSQL("INSERT INTO DiseaseBadProduct(\"Disease\", \"Product\") VALUES($1, $2)", arrayOf("Гастрит", "Пирожное"))

            db.execSQL("INSERT INTO Dish(\"Name\", \"Kcal\") VALUES($1, $2)", arrayOf("", ""))

            db.execSQL("INSERT INTO DishProduct(\"Dish\", \"Product\") VALUES($1, $2)", arrayOf("", ""))

            db.close()
        }
    }
}