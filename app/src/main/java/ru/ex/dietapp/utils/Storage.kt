package ru.ex.dietapp.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONObject

class Storage {
    companion object {
        val product = mutableMapOf<String, Int>()
        val disease: MutableMap<String, DiseaseData> = mutableMapOf()
        var ration: JSONObject? = null

        // загрузка данных бд в память
        fun initStorage(activity: Activity) {
            val db: SQLiteDatabase =
                activity.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null)


            var cursor: Cursor = db.rawQuery("select * from Product", null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val name = cursor.getString(cursor.getColumnIndex("Name"))
                    val kcal = cursor.getInt(cursor.getColumnIndex("Kcal"))
                    product[name] = kcal
                    cursor.moveToNext()
                }
            }

            cursor = db.rawQuery("select * from Disease", null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val name = cursor.getString(cursor.getColumnIndex("Name"))
                    val maxKcal = cursor.getInt(cursor.getColumnIndex("MaxKcal"))
                    val data = DiseaseData()
                    data.maxKcal = maxKcal

                    disease[name] = data

                    cursor.moveToNext()
                }
            }

            for ((k, _) in disease) {
                cursor = db.rawQuery("select * from DiseaseBadProduct where Disease=$1", arrayOf(k))
                val arrayProduct = arrayListOf<String>()
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val product = cursor.getString(cursor.getColumnIndex("Product"))
                    arrayProduct.add(product)
                    cursor.moveToNext()
                }
                disease[k]?.product = arrayProduct
            }

            cursor.close()
        }
    }

    class DiseaseData {
        var product = arrayListOf<String>()
        var maxKcal = 0
    }
}