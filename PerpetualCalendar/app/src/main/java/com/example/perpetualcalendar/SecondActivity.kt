package com.example.perpetualcalendar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val extras=intent.extras ?: return
        val choosen_year =extras.getInt("Year")



        sundays_back_button.setOnClickListener{
            val data= Intent()
            data.putExtra("returning_Year",choosen_year)
            setResult(Activity.RESULT_OK, data)
            finish()
        }



        if(choosen_year<2020)
        {
            sundays_of_year.text = "Przepisy dotyczące niedziel handlowych obowiązują od 2020 roku.\nProszę wprowadź rok 2020 lub późniejszy!"
            return
        }

        val calendar: Calendar = Calendar.getInstance()
        val format: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        calendar.time = format.parse(getEasterDate(choosen_year.toString()))!!



        val sundays_list = ArrayList<String>()

        //sunday before easter
        while(true)
        {
            calendar.add(Calendar.DAY_OF_YEAR,-1)
            if(calendar.get(Calendar.DAY_OF_WEEK)==1)break
        }
        sundays_list.add(format.format(calendar.time))

        //sunday before x-mas
        calendar.set(calendar.get(Calendar.YEAR),Calendar.DECEMBER,25)

        var num_of_sundays=0
        while(true)
        {
            calendar.add(Calendar.DAY_OF_YEAR,-1)
            if(calendar.get(Calendar.DAY_OF_WEEK)==1)
            {
                num_of_sundays+=1
                sundays_list.add(format.format(calendar.time))
            }
            if(num_of_sundays==2)break
        }



        for((month,last_day) in listOf(Calendar.JANUARY,Calendar.APRIL,Calendar.JUNE, Calendar.AUGUST).zip(listOf(31,30,30,31))) {
            //last sunday of desired months
            calendar.set(calendar.get(Calendar.YEAR), month, last_day)
            while (true) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == 1) break
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            }
            if(format.format(calendar.time) !in sundays_list) sundays_list.add(format.format(calendar.time))
        }

        //prepare output

        //sort list of sundays
        val sorted_sundays_list=sundays_list.sortedWith(Comparator<String>{ a, b ->
            val a_calendar: Calendar = Calendar.getInstance()
            a_calendar.time = format.parse(a)!!

            val b_calendar: Calendar = Calendar.getInstance()
            b_calendar.time = format.parse(b)!!

            when {
                a_calendar.before(b_calendar) -> -1
                b_calendar.before(a_calendar) -> 1
                else -> 0
            }
        })

        var output="Niedziele handlowe w roku $choosen_year:\n\n"

        for (i in sorted_sundays_list)
            output+="• "+i+"\n"

        sundays_of_year.text = output
    }


}