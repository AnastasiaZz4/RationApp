package ru.ex.dietapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_first_launch.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_setting_ration.*
import ru.ex.dietapp.models.RationRepository
import ru.ex.dietapp.utils.Glossary
import ru.ex.dietapp.utils.Storage
import ru.ex.dietapp.utils.Utils

class SettingRationActivity : AppCompatActivity() {

    val MILLS_IN_DAY = 86400000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_ration)

        // установка анимации перехода в следующему activity
        overridePendingTransition(
            R.anim.activity_default_enter,
            R.anim.activity_default_exit
        )

        // создание адаптера для ввода предпочитаемых продуктов
        val goodAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, Storage.product.keys.toTypedArray())
        elemGoodProductInput.threshold = 1
        elemGoodProductInput.setAdapter(goodAdapter)

        // создание адаптера для ввода нежелательных продуктов
        val badAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, Storage.product.keys.toTypedArray())
        elemBadProductInput.threshold = 1
        elemBadProductInput.setAdapter(badAdapter)

        // событие на добавление продукта
        elemAddGoodProduct.setOnClickListener {
            // получение названия продукта без пробелом по бокам
            val value = elemGoodProductInput.text.trim().toString()

            // проверка на пустоту значения
            if (value == "") {
                return@setOnClickListener
            }

            // существует ли этот продукт в списке продуктов
            if (RationRepository.listGoodProduct.contains(value)) {
                return@setOnClickListener
            }

            // добавление продукта в список желательных продуктов
            if (Storage.product.keys.contains(value)) {
                RationRepository.listGoodProduct.add(value)
            }

            // визуальных вывод продукта
            val view = Button(this, null, R.attr.materialButtonOutlinedStyle)
            view.setTextColor(ContextCompat.getColor(this, R.color.success))
            view.text = value
            // установка события удаления продукта при клике по нему
            view.setOnClickListener {
                RationRepository.listGoodProduct.remove(value)
                elemContainerGoodProduct.removeView(it)
            }

            elemContainerGoodProduct.addView(view)

            elemGoodProductInput.text.clear()
        }

        // событие на добавление продукта
        elemAddBadProduct.setOnClickListener {
            // получение названия продукта без пробелом по бокам
            val value = elemBadProductInput.text.trim().toString()

            // проверка на пустоту значения
            if (value == "") {
                return@setOnClickListener
            }

            // существует ли этот продукт в списке продуктов
            if (RationRepository.listBadProduct.contains(value)) {
                return@setOnClickListener
            }

            // добавление продукта в список желательных продуктов
            if (Storage.product.keys.contains(value)) {
                RationRepository.listBadProduct.add(value)
            }

            // визуальных вывод продукта
            val view = Button(this, null, R.attr.materialButtonOutlinedStyle)
            view.setTextColor(ContextCompat.getColor(this, R.color.danger))
            view.text = value
            // установка события удаления продукта при клике по нему
            view.setOnClickListener {
                RationRepository.listBadProduct.remove(value)
                elemContainerBadProduct.removeView(it)
            }

            elemContainerBadProduct.addView(view)

            elemBadProductInput.text.clear()
        }

        // событие на выбор даты начала рациона
        elemDateStart.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Выберите день, с которого стоит начать расчёт")
                    .build()

            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                RationRepository.dateStart = it
                elemDateStart.text = "с %s %s %s года".format(
                    Utils.getDate(it, "dd"),
                    Glossary.MonthCase[Utils.getDate(it, "MM")?.toInt()!!],
                    Utils.getDate(it, "yyyy")
                )
            }
        }

        // событие на выбор даты конца рациона
        elemDateEnd.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Выберите день, по который стоит произвести расчёт")
                    .build()

            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                RationRepository.dateEnd = it + MILLS_IN_DAY
                elemDateEnd.text = "по %s %s %s года".format(
                    Utils.getDate(it, "dd"),
                    Glossary.MonthCase[Utils.getDate(it, "MM")?.toInt()!!],
                    Utils.getDate(it, "yyyy")
                )
            }
        }

        // событие на кнопку "Рассчитать"
        elemRationCalculating.setOnClickListener {
            if (
                RationRepository.dateStart == (0).toLong() ||
                RationRepository.dateEnd == (0).toLong()
            ) {
                Snackbar.make(elemLayoutSetting, "Похоже, что какое-то поле с датой пустое!", Snackbar.LENGTH_LONG)
                    .show()
               return@setOnClickListener
            }
            val intent = Intent(this, RationActivity::class.java)
            startActivity(intent)
        }
    }
}