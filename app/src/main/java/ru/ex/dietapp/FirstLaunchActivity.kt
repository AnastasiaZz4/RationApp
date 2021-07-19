package ru.ex.dietapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_first_launch.*
import org.json.JSONArray
import org.json.JSONObject
import ru.ex.dietapp.models.UserData
import ru.ex.dietapp.utils.Glossary
import ru.ex.dietapp.utils.Storage
import ru.ex.dietapp.utils.Utils

class FirstLaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch)

        // анимация перехода между activity
        overridePendingTransition(
            R.anim.activity_default_enter,
            R.anim.activity_default_exit
        )

        // инициализация переменных для работы с локальным хранилицем
        val preferenceManager = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferenceManager.edit()

        // получение ранее сохранённых данных о составленном рационе в формате строки
        val stringRationData = preferenceManager.getString("ration", null)

        if (stringRationData != null) {
            Storage.initStorage(this)
            Storage.ration = JSONObject(stringRationData)

            // получение ранее сохранённых данных пользователя
            UserData.name = preferenceManager.getString("name", "")!!
            UserData.disease = preferenceManager.getString("disease", "")!!
            UserData.dob = preferenceManager.getLong("dob", 0)

            val intent = Intent(this, RationActivity::class.java)
            startActivity(intent)
            finishAffinity()

            return
        }

        // получение ранее сохранённого имени пользователя
        val userName = preferenceManager.getString("name", "")
        if (userName != "") {
            UserData.name = userName!!
            UserData.disease = preferenceManager.getString("disease", "")!!
            UserData.dob = preferenceManager.getLong("dob", 0)

            // загрузка данных бд в память
            Storage.initStorage(this)

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finishAffinity()
            return
        } else {
            // создание базы данных на телефоне
            Utils.initDataBase(this)
            // загрузка данных бд в память
            Storage.initStorage(this)
        }

        // событие на установку даты рождения
        elemDOB.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Выберите дату рождения")
                    .build()

            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                UserData.dob = it
                elemDOB.text = "Дата рождения: %s %s %s года".format(
                    Utils.getDate(it, "dd"),
                    Glossary.MonthCase[Utils.getDate(it, "MM")?.toInt()!!],
                    Utils.getDate(it, "yyyy")
                )
            }
        }

        // событие на установку болезни
        elemDisease.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val arrayDisease = arrayListOf<String>()

            for ((k, _) in Storage.disease) {
                arrayDisease.add(k)
            }

            builder.setTitle("Выберите ваше заболевание")
                .setItems(arrayDisease.toTypedArray()) { _, which ->
                    UserData.disease = arrayDisease[which]
                    elemDisease.text = "Заболевание: %s".format(arrayDisease[which])
                }

            builder.create().show()
        }

        // событие на кнопку "Продолжить"
        elemNextScreen.setOnClickListener {
            val zero = 0
            val textName = elemName.text?.trim().toString()

            when {
                textName.isEmpty() -> {
                    Snackbar.make(elemConstraint, "Похоже, что поле с вашим именем пустое!", Snackbar.LENGTH_LONG)
                        .show()
                }
                UserData.disease.isEmpty() -> {
                    Snackbar.make(elemConstraint, "Похоже, что поле с вашим заболеванием пустое!", Snackbar.LENGTH_LONG)
                        .show()
                }
                UserData.dob == zero.toLong() -> {
                    Snackbar.make(elemConstraint, "Похоже, что поле с вашей датой рождения пустое!", Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {
                    UserData.name = textName

                    // сохранение данных пользователя в локальное хранилище
                    editor.putString("name", UserData.name)
                    editor.putString("disease", UserData.disease)
                    editor.putLong("dob", UserData.dob)
                    editor.apply()

                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }
    }
}