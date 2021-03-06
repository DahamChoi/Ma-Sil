package com.gsc.silverwalk.ui.fragment.mission

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.gsc.silverwalk.ui.domission.DoMissionActivity
import com.gsc.silverwalk.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MissionStartDialog : DialogFragment() {

    private lateinit var weatherInfo: MissionWeather

    private var time: Long? = null
    private var location: String? = null
    private var type: String? = null
    private var level: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dialogView = inflater.inflate(R.layout.dialog_mission_start, container, false)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm a", Locale.US)
        val formatted = current.format(formatter)
        dialogView.findViewById<TextView>(R.id.dialog_start_time_textview)
            .setText(formatted)

        dialogView.findViewById<Button>(R.id.mission_start_dialog_start_button)
            .setOnClickListener(View.OnClickListener {
                val i = Intent(context, DoMissionActivity::class.java)
                i.putExtra("missionTime",time)
                i.putExtra("missionLocation",location)
                i.putExtra("missionType",type)
                i.putExtra("missionLevel",level)
                startActivity(i)
                dialog?.dismiss()
            })

        dialogView.findViewById<ImageView>(R.id.dialog_start_weather_image).setImageResource(
            weatherInfo.getWeatherIconId())
        dialogView.findViewById<TextView>(R.id.dialog_start_display_2_textview).setText(
            weatherInfo.getTemperatureComment(requireContext()))
        dialogView.findViewById<TextView>(R.id.dialog_start_temperature_textview)
            .setText(weatherInfo.temperature)
        dialogView.findViewById<TextView>(R.id.dialog_start_temperature_info_text)
            .setText(weatherInfo.weather_text)

        return dialogView
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setDialogInfo(time: Long?, location: String?, type: String?, level: Long?) {
        this.time = time;
        this.location = location;
        this.type = type;
        this.level = level;
    }

    fun setDialogWeatherInfo(temperature : String?, temperature_text : String?){
        weatherInfo = MissionWeather(temperature, temperature_text)
    }
}