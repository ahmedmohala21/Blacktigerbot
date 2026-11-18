
package com.blacktiger.bot

import android.app.Activity
import android.content.Intent
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import android.graphics.Color

class MainActivity : Activity() {

    private val orange = Color.rgb(255, 145, 0)
    private val dark = Color.rgb(18, 20, 24)

    private val gamePackage = "com.igg.android.conquerors"

    private lateinit var status: TextView
    private lateinit var levelInput: EditText
    private lateinit var slots: MutableList<Spinner>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
    }

    private fun buildScreen() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 24, 22, 24)
            setBackgroundColor(dark)
        }

        val scroll = ScrollView(this)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, 20)
        }

        val title = TextView(this).apply {
            text = "BLACK TIGER BOT"
            textSize = 27f
            setTextColor(orange)
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 8)
        }

        val subtitle = TextView(this).apply {
            text = "مساعد حدث نداء الجيوش"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 18)
        }

        content.addView(title)
        content.addView(subtitle)

        status = TextView(this).apply {
            text = "الحالة: جاهز"
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(14, 14, 14, 14)
            setBackgroundColor(Color.rgb(38, 42, 48))
        }

        content.addView(status, match())

        content.addView(label("مستوى التطوير المستهدف"))

        levelInput = EditText(this).apply {
            setText("10")
            setTextColor(Color.WHITE)
            hint = "مثال: 10"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setSingleLine(true)
            setPadding(12, 8, 12, 8)
            setBackgroundColor(Color.rgb(45, 48, 54))
        }

        content.addView(levelInput, match())

        content.addView(label("نوع المبنى لكل مكان"))

        slots = mutableListOf()

        val choices = arrayOf(
            "غير محدد",
            "حدادة",
            "مزرعة",
            "منشرة"
        )

        for (i in 1..10) {

            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, 5, 0, 5)
            }

            val number = TextView(this).apply {
                text = "المكان $i"
                textSize = 15f
                setTextColor(Color.WHITE)
            }

            val spinner = Spinner(this).apply {
                adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    choices
                )
            }

            slots.add(spinner)

            row.addView(
                number,
                LinearLayout.LayoutParams(0, -2, 1f)
            )

            row.addView(
                spinner,
                LinearLayout.LayoutParams(0, -2, 1.5f)
            )

            content.addView(row, match())
        }

        content.addView(label("التحكم"))

        val openGame = button("فتح لعبة الفاتحون") {
            openGame()
        }

        content.addView(openGame, match())

        val accessibility = button("إعدادات إمكانية الوصول") {
            startActivity(
                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            )
        }

        content.addView(accessibility, match())

        val start = button("بدء المتابعة") {

            val level = levelInput.text.toString().toIntOrNull()

            if (level == null || level < 1) {
                toast("اكتب مستوى صحيحًا أولًا")
            } else {
                status.text =
                    "الحالة: الإعدادات محفوظة — الأتمتة لم تبدأ بعد"

                toast("تم تسجيل الإعدادات")
            }
        }

        content.addView(start, match())

        val stop = button("إيقاف") {
            status.text = "الحالة: متوقف"
        }

        content.addView(stop, match())

        val note = TextView(this).apply {
            text = "هذه نسخة تطويرية. فتح اللعبة متاح، أما البناء والتطوير والهدم التلقائي فما زالت قيد البرمجة."
            textSize = 13f
            setTextColor(Color.LTGRAY)
            setPadding(4, 18, 4, 8)
        }

        content.addView(note, match())

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        setContentView(root)
    }

    private fun openGame() {

        try {
            val intent =
                packageManager.getLaunchIntentForPackage(gamePackage)

            if (intent != null) {
                status.text = "الحالة: جاري فتح لعبة الفاتحون"
                startActivity(intent)
            } else {
                status.text = "الحالة: لم يتم العثور على اللعبة"
                toast("تأكد أن لعبة الفاتحون مثبتة")
            }

        } catch (e: ActivityNotFoundException) {
            toast("تعذر فتح اللعبة")
        } catch (e: Exception) {
            toast("حدث خطأ أثناء فتح اللعبة")
        }
    }

    private fun label(value: String) = TextView(this).apply {
        text = value
        textSize = 16f
        setTextColor(orange)
        setPadding(0, 18, 0, 8)
    }

    private fun button(
        value: String,
        action: () -> Unit
    ) = Button(this).apply {
        text = value
        setTextColor(Color.BLACK)
        setBackgroundColor(orange)
        setOnClickListener {
            action()
        }
    }

    private fun match() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        bottomMargin = 8
    }

    private fun toast(value: String) {
        Toast.makeText(
            this,
            value,
            Toast.LENGTH_LONG
        ).show()
    }
}
