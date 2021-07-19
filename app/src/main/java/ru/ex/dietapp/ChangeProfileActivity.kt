package ru.ex.dietapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_change_profile.*
import kotlinx.android.synthetic.main.activity_first_launch.*
import kotlinx.android.synthetic.main.activity_profile.elemDOB
import kotlinx.android.synthetic.main.activity_profile.elemDisease
import kotlinx.android.synthetic.main.activity_profile.elemName
import ru.ex.dietapp.models.UserData
import ru.ex.dietapp.utils.Glossary
import ru.ex.dietapp.utils.Storage
import ru.ex.dietapp.utils.Utils

class ChangeProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        // установка анимации перехода activity
        overridePendingTransition(
            R.anim.activity_default_enter,
            R.anim.activity_default_exit
        )

        // инициализация переменных для работы с локальным хранилицем
        val preferenceManager = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferenceManager.edit()

        // установка данных профиля, которые актуальны на момент открытия activity
        elemName.text = UserData.name
        elemDOB.text = "Дата рождения: %s %s %s года".format(
            Utils.getDate(UserData.dob, "dd"),
            Glossary.MonthCase[Utils.getDate(UserData.dob, "MM")?.toInt()!!],
            Utils.getDate(UserData.dob, "yyyy")
        )
        elemDisease.text = UserData.disease

        // установка события на смену даты дня рождения
        elemDOB.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Выберите дату рождения")
                    .build()

            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                UserData.dob = it
                elemDOB.text = "Дата рождения: %s %s %s года".format(
                    Utils.getDate(UserData.dob, "dd"),
                    Glossary.MonthCase[Utils.getDate(UserData.dob, "MM")?.toInt()!!],
                    Utils.getDate(UserData.dob, "yyyy")
                )
            }
        }

        // установка события на смену заболевания
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

        // установка события на кнопку "Сохранить"
        elemSaveChange.setOnClickListener {
            val zero = 0
            val textName = elemName.text?.trim().toString()

            when {
                textName.isEmpty() -> {
                    Snackbar.make(elemChangeProfileConst, "Похоже, что поле с вашим именем пустое!", Snackbar.LENGTH_LONG)
                        .show()
                }
                UserData.disease.isEmpty() -> {
                    Snackbar.make(elemChangeProfileConst, "Похоже, что поле с вашим заболеванием пустое!", Snackbar.LENGTH_LONG)
                        .show()
                }
                UserData.dob == zero.toLong() -> {
                    Snackbar.make(elemChangeProfileConst, "Похоже, что поле с вашей датой рождения пустое!", Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {
                    UserData.name = textName

                    editor.putString("name", UserData.name)
                    editor.putString("disease", UserData.disease)
                    editor.putLong("dob", UserData.dob)
                    editor.apply()

                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}