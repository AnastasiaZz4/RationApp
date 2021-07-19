package ru.ex.dietapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_ration.*
import org.json.JSONArray
import org.json.JSONObject
import ru.ex.dietapp.models.RationRepository
import ru.ex.dietapp.models.UserData
import ru.ex.dietapp.utils.Storage
import java.time.LocalDateTime
import java.util.*

class RationActivity : AppCompatActivity() {

    val MILLS_IN_DAY = 86400000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ration)

        // установка анимации перехода в следующему activity
        overridePendingTransition(
            R.anim.activity_default_enter,
            R.anim.activity_default_exit
        )

        // инициализация переменных для работы с локальным хранилицем
        val preferenceManager = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferenceManager.edit()

        // провека на ранее сохранённый рацион
        if (preferenceManager.getString("ration", null) == null) {
            var listProduct = mutableListOf<String>()
            for ((k, _) in Storage.product) {
                listProduct.add(k)
            }

            // исключение повторяющихся элементов в массиве с продуктами
            listProduct = listProduct.toMutableSet().toMutableList()

            // исключение нежелательных продуктов, которые указал пользователь
            for (i in 0 until RationRepository.listBadProduct.size) {
                val badProduct = RationRepository.listBadProduct[i]
                listProduct.remove(badProduct)
            }

            // исключение продуктов, которые запрещены при болезне
            for (i in 0 until Storage.disease[UserData.disease]?.product?.size!!) {
                val badProduct = Storage.disease[UserData.disease]?.product?.get(i)
                listProduct.remove(badProduct)
            }

            val jsonRationData = JSONObject()
            var currentDate: Long = RationRepository.dateStart

            // создание рациона на день, сумма ккал которых не привышает
            // максимальную допустимую норму в день при текущей болезни
            while (RationRepository.dateEnd >= currentDate) {
                val jsonProduct = JSONArray()

                listProduct.shuffle()

                var sumKcal = 0
                val maxKcal = Storage.disease[UserData.disease]?.maxKcal

                for (i in 0 until listProduct.size) {
                    val product = listProduct[i]
                    if ((sumKcal + Storage.product[product]!!) < maxKcal!!) {
                        sumKcal += Storage.product[product]!!

                        jsonProduct.put(product)

                    } else {
                        for (j in i until listProduct.size) {
                            if ((sumKcal + Storage.product[product]!!) < maxKcal!!) {
                                sumKcal += Storage.product[product]!!

                                jsonProduct.put(product)
                                break
                            }
                        }
                    }
                }

                jsonRationData.put(currentDate.toString(), jsonProduct)

                currentDate += MILLS_IN_DAY
            }

            // сохранение сгенерированного рациона в локальное хранилище
            editor.putString("ration", jsonRationData.toString())
            editor.apply()

            // сохранение сгенерированного рациона в память
            Storage.ration = jsonRationData
        } else {
            // загружаем ранее сохранённый рацион в память
            Storage.ration = JSONObject(preferenceManager.getString("ration", null))

            val arrayDateLong = arrayListOf<Long>()
            for (stringDate in Storage.ration!!.keys()) {
                arrayDateLong.add(stringDate.toLong())
            }
            // получение диапазона дат из уже сохранённого рациона
            RationRepository.dateStart = arrayDateLong.minOrNull()!!
            RationRepository.dateEnd = arrayDateLong.maxOrNull()!!
        }

        // отрытие фрагмента с рационом на сегодняшний день
        // получить начало сегодняшнего дня
        var currentDate = Calendar.getInstance().timeInMillis
        currentDate = (currentDate / MILLS_IN_DAY) * MILLS_IN_DAY

        // установка адаптера с фрагментами
        val adapter = adapterDay(supportFragmentManager)
        elemDayRationController.adapter = adapter

        // если в сгенерированном рационе есть сегодняшняя дата
        if (currentDate >= RationRepository.dateStart && currentDate <= RationRepository.dateEnd) {
            // то открывает этот день с рационом
            elemDayRationController.currentItem = ((currentDate / MILLS_IN_DAY) - (RationRepository.dateStart / MILLS_IN_DAY)).toInt()
        }

        // событие на кнопку вернуться в профиль
        elemBackInProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    inner class adapterDay(manager: FragmentManager) :FragmentPagerAdapter(manager) {
        // количество фрагментов
        override fun getCount(): Int {
            return ((RationRepository.dateEnd - RationRepository.dateStart) / MILLS_IN_DAY).toInt()
        }

        // получить фрагмент с определённой позицией
        override fun getItem(position: Int): Fragment {
            val date = RationRepository.dateStart + (MILLS_IN_DAY * position)
            return DayRationFragment(date)
        }

    }
}