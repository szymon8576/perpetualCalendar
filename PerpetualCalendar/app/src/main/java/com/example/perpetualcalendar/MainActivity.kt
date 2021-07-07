package com.example.perpetualcalendar


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker.OnValueChangeListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun getEasterDate(input_year: String) : String {
    val a: Int
    val b: Int
    val c: Int
    val d: Int
    val e: Int
    val f: Int
    val g: Int
    val h: Int
    val i: Int
    val k: Int
    val l: Int
    val m: Int
    val p: Int

    val year=input_year.toInt()
    a = year%19
    b = year / 100
    c = year % 100
    d = b / 4
    e = b % 4
    f = ((b + 8) / 25)
    g = ((b - f + 1) / 3)
    h = (19 * a + b - d - g + 15) % 30
    i = c / 4
    k = c % 4
    l = (32 + 2 * e + 2 * i - h - k) % 7
    m = ((a + 11 * h + 22 * l) / 451)
    p = (h + l - 7 * m + 114) % 31

    val day = p + 1
    val month = ((h + l - 7 * m + 114) / 31)


    return String.format("%02d", day)+'-'+String.format("%02d", month)+'-'+input_year

}

fun calcPrintRes(input_year: String): String {
    val calendar: Calendar = Calendar.getInstance()
    val format: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    calendar.time = format.parse(getEasterDate(input_year))!!

    //wielkanoc
    val wielkanoc ="\nWielkanoc: " + format.format(calendar.time)

    //popielec
    calendar.add(Calendar.DAY_OF_YEAR, -46)
    val popielec="Popielec: "+format.format(calendar.time)

    //boze cialo
    calendar.add(Calendar.DAY_OF_YEAR, 106)
    val boze_cialo="\nBoże Ciało: "+format.format(calendar.time)

    //adwent
    calendar.set(calendar.get(Calendar.YEAR),Calendar.DECEMBER,25)

    var numOfSundays=0

    while(true)
    {
        calendar.add(Calendar.DAY_OF_YEAR,-1)
        if(calendar.get(Calendar.DAY_OF_WEEK)==1)numOfSundays+=1
        if(numOfSundays==4)break

    }


    val adwent="\nAdwent: "+format.format(calendar.time)

    return popielec+wielkanoc+boze_cialo+adwent


}

class MainActivity : AppCompatActivity() {

        override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode==1000)&&(resultCode==Activity.RESULT_OK))
        {
            if(data!=null){
                if(data.hasExtra("returning_Year")){
                    yearPicker.value=data.extras?.getInt("returning_Year")!!
                    return
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val initYear =Calendar.getInstance().get(Calendar.YEAR)

        yearPicker.minValue=1900
        yearPicker.maxValue=2200
        yearPicker.value=initYear




        //initial result
        main_data.text = calcPrintRes(initYear.toString())

        //sundays activity
        sundaysButton.setOnClickListener{
                val intent=Intent(this,SecondActivity::class.java)
                intent.putExtra("Year",yearPicker.value)
                startActivityForResult(intent, 1000)
        }

        //'days between' activity
        days_between_button.setOnClickListener{
            val intent=Intent(this,ThirdActivity::class.java)
            startActivity(intent)
        }

        //year scroll
        yearPicker.setOnValueChangedListener { _, _, newVal ->
            main_data.text = calcPrintRes(newVal.toString())
        }

    }
}