package com.example.perpetualcalendar

import android.os.Bundle
import android.widget.DatePicker.OnDateChangedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_third.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun calcDays(start_date: String, end_date: String) : String {



    val format: DateFormat = SimpleDateFormat("dd-MM-yyyy")

    val temp_start_cal: Calendar = Calendar.getInstance()
    temp_start_cal.time = format.parse(start_date)!!

    val temp_end_cal: Calendar = Calendar.getInstance()
    temp_end_cal.time = format.parse(end_date)!!

    if(temp_end_cal.before(temp_start_cal))return "Podaj poprawny zakres dat!"


    val calendar: Calendar = Calendar.getInstance()


    //ruchome swieta - boze cialo i pn wielkanocny
    val startYear=start_date.takeLast(4).toInt()
    val endYear=end_date.takeLast(4).toInt()

    val freedays_list = ArrayList<String>()


    for (i in startYear..endYear)
    {
        calendar.time = format.parse(getEasterDate(i.toString()))!!
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        freedays_list.add(format.format(calendar.time))

        calendar.add(Calendar.DAY_OF_YEAR, 59)
        freedays_list.add(format.format(calendar.time))

        freedays_list.add("01-01-" + i)
        freedays_list.add("06-01-" + i)
        freedays_list.add("01-05-" + i)
        freedays_list.add("03-05-" + i)
        freedays_list.add("15-08-" + i)
        freedays_list.add("01-11-" + i)
        freedays_list.add("11-11-" + i)
        freedays_list.add("25-12-" + i)
        freedays_list.add("26-12-" + i)

    }

    calendar.time = format.parse(start_date)!!

    val endCalendar: Calendar = Calendar.getInstance()
    endCalendar.time = format.parse(end_date)!!

    val totalNumOfDays= TimeUnit.MILLISECONDS.toDays(endCalendar.timeInMillis - calendar.timeInMillis)

    var workdaysNum=0
    while(calendar.before(endCalendar))
    {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) ==7)continue
        else if (format.format(calendar.time) in freedays_list) continue
        workdaysNum+=1

    }

    return "Dni roboczych: "+workdaysNum.toString()+"\nDni kalendarzowych: "+totalNumOfDays.toString()
}

fun dateNumToStr(day: Int, month: Int, year: Int):String
{
    return String.format("%02d", day)+'-'+String.format("%02d", month)+'-'+year.toString()
}

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        var startDateVar= dateNumToStr(start_date.dayOfMonth, start_date.month + 1, start_date.year)
        var endDateVar= dateNumToStr(end_date.dayOfMonth, end_date.month + 1, end_date.year)

        //init value
        daysCountRes.text = calcDays(startDateVar, endDateVar)

        days_count_return.setOnClickListener{
            finish()
        }

        // functions commented below worked, but using .setOnDateChangedListener required API>=26.
        // In order to be able to run this app on earlier versions of Android (including my phone with 7.1.2 Android), I used .init() function instead of .setOnDateChangedListener()

        /*
        start_date.setOnDateChangedListener{view,year,month,day->
            start_date_var=date_num_to_str(day,month+1,year)
            days_count_res.setText(calc_days(start_date_var,end_date_var))
        }

        end_date.setOnDateChangedListener{view,year,month,day->
            end_date_var=date_num_to_str(day,month+1,year)
            days_count_res.setText(calc_days(start_date_var,end_date_var))
        }
            */

        start_date.init(start_date.year,start_date.month ,start_date.dayOfMonth
        ) { _, year, month, day ->
            startDateVar = dateNumToStr(day, month + 1, year)
            daysCountRes.text = calcDays(startDateVar, endDateVar)
        }

        end_date.init(end_date.year,end_date.month,end_date.dayOfMonth
        ) { _, year, month, day ->
            endDateVar = dateNumToStr(day, month + 1, year)
            daysCountRes.text = calcDays(startDateVar, endDateVar)
        }

    }
}