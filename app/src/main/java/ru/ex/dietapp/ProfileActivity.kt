package ru.ex.dietapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject
import ru.ex.dietapp.models.UserData
import ru.ex.dietapp.utils.Glossary
import ru.ex.dietapp.utils.Storage
import ru.ex.dietapp.utils.Utils
import kotlin.system.exitProcess

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // установка анимации перехода в следующему activity
        overridePendingTransition(
            R.anim.activity_default_enter,
            R.anim.activity_default_exit
        )

        // инициализация переменных для работы с локальным хранилицем
        val preferenceManager = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferenceManager.edit()
        Storage.initStorage(this)

        // вывод информации о пользователе
        elemName.text = "Меня зовут %s".format(UserData.name)
        elemDOB.text = "Дата рождения: %s %s %s года".format(
            Utils.getDate(UserData.dob, "dd"),
            Glossary.MonthCase[Utils.getDate(UserData.dob, "MM")?.toInt()!!],
            Utils.getDate(UserData.dob, "yyyy")
        )
        elemDisease.text = "Моё заболевание: %s".format(UserData.disease)

        // получение ранее сохранённых данных о составленном рационе в формате строки
        val rationData = preferenceManager.getString("ration", null)
        if (rationData != null) {
            Storage.ration = JSONObject(rationData)
            elemStartRation.visibility = View.GONE

            elemShowRation.visibility = View.VISIBLE
            elemResetRation.visibility = View.VISIBLE

            // событие на кнопку "Посмотреть созданную диету"
            elemShowRation.setOnClickListener {
                val intent = Intent(this, RationActivity::class.java)
                startActivity(intent)
            }

            // событие на кнопку "Сбросить диету"
            elemResetRation.setOnClickListener {
                editor.putString("ration", null)
                editor.apply()
                elemShowRation.visibility = View.GONE
                elemStartRation.visibility = View.VISIBLE
                elemResetRation.visibility = View.GONE
            }
        }

        // событие на кнопку "Редактировать" в блоке информации профиля
        elemProfileChange.setOnClickListener {
            val intent = Intent(this, ChangeProfileActivity::class.java)
            startActivity(intent)
        }

        // событие на кнопку "Рассчитать рацион"
        elemStartRation.setOnClickListener {
            val intent = Intent(this, SettingRationActivity::class.java)
            startActivity(intent)
        }

        // событие на кнопку "Выйти из приложения"
        elemExitApp.setOnClickListener {
            finishAffinity()
        }

    }
}