package ru.ex.dietapp

import android.os.Bundle
import android.service.autofill.UserData
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import kotlinx.android.synthetic.main.fragment_day_ration.*
import org.json.JSONArray
import org.json.JSONObject
import ru.ex.dietapp.utils.Glossary
import ru.ex.dietapp.utils.Storage
import ru.ex.dietapp.utils.Utils
import java.util.*

class DayRationFragment(private val date: Long) : Fragment() {

    val MILLS_IN_DAY = 86400000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_day_ration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // вывод даты, на которую показан список продуктов
        elemDate.text = "%s %s %s года".format(
            Utils.getDate(date, "dd"),
            Glossary.MonthCase[Utils.getDate(date, "MM")?.toInt()!!],
            Utils.getDate(date, "yyyy")
        )

        // проверка даты, не сегодня ли она
        if ((Calendar.getInstance().timeInMillis / MILLS_IN_DAY) * MILLS_IN_DAY == date) {
            elemDate.text = "%s (сегодня)".format(elemDate.text)
        }

        // провека, не пустое ли хранилище с продуктами
        if (Storage.product.isEmpty()) {
            Storage.initStorage(requireActivity())
        }

        // инициализация переменной, хранящей массив продуктов на текущий день
        val listProduct = Storage.ration?.get(date.toString()) as JSONArray

        // визуальный вывод продуктов на экран
        for (i in 0 until listProduct.length()) {
            val product = listProduct[i] as String
            appendProductRow(product, Storage.product[product])
        }
    }

    // создание view с продуктом и вывод его на экран
    private fun appendProductRow(name: String, kcal: Int?) {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.HORIZONTAL

        val product = TextView(requireContext())
        product.text = name
        product.setPadding(18)
        product.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

        val worth = TextView(requireContext())
        val worthParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        worth.layoutParams = worthParams
        worth.text = "$kcal ккал"
        worth.setPadding(18)
        worth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        worth.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END

        layout.addView(product)
        layout.addView(worth)

        elemInfo.addView(layout)
    }
}