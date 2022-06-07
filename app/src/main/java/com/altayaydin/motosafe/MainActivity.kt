package com.altayaydin.motosafe

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.graphics.toColorInt
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun calculate(view : View){
        var speed1 = findViewById<EditText>(R.id.firstSpeedInput).text.toString().toDoubleOrNull()
        var speed2 = findViewById<EditText>(R.id.secondSpeedInput).text.toString().toDoubleOrNull()

        var speed1Control : Boolean = false
        var speed2Control : Boolean = false
        var differenceControl: Boolean = false

        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator



        //Check if the textbox is empty.
        if (speed1 == null){
            speed1Control = false
            findViewById<EditText>(R.id.firstSpeedInput).error = "Cannot be blank!"
        }

        else{
            speed1Control = true
        }

        println("speed1Control: " + speed1Control) //Print to console for developer.

        //Check if the textbox is empty.
        if (speed2 == null){
            speed2Control = false
            findViewById<EditText>(R.id.secondSpeedInput).error = "Cannot be blank!"
        }

        else{
            speed2Control = true
        }

        println("speed2Control: " + speed2Control) //Print to console for developer.

        if (speed2 != null && speed1 != null) {
            if (speed2 > speed1){
                differenceControl = true

            } else{
                differenceControl = false
                findViewById<TextView>(R.id.tipTextView).setTextColor(Color.parseColor("#ff0000"))

                //Changing the color to red in order to warn the user.

                Handler().postDelayed({
                    findViewById<TextView>(R.id.tipTextView).setTextColor(Color.parseColor("#000000"))
                }, 500)
            }
        }

        println("differenceControl: " + differenceControl) //Print to console for developer.

        var totalControl : Boolean = (speed1Control && speed2Control && differenceControl) //Total check for null textboxes.
        println("totalControl: " + totalControl) //Print to console for developer.

        if (totalControl == true){
            var reaction = 0.27777 //Constant reaction time considering human.
            var reactionTime1 = speed1?.times(reaction)?.toBigDecimal()?.setScale(2, RoundingMode.UP)
                ?.toDouble()
            var reactionTime2 = speed2?.times(reaction)?.toBigDecimal()?.setScale(2, RoundingMode.UP)
                ?.toDouble()
            var breakingDistance1 = speed1?.let { Math.pow(it, 2.0) }?.div(200)
            var breakingDistance2 = speed2?.let { Math.pow(it, 2.0) }?.div(200)
            var totalDistance1 = breakingDistance1?.let { reactionTime1?.plus(it) }
            var totalDistance2 = breakingDistance2?.let { reactionTime2?.plus(it) }
            var passSpeedTotal : Double = 0.0
            if (reactionTime2!! <= totalDistance1!!) {
                var passSpeed =
                    (totalDistance2!! - reactionTime2) - (totalDistance2 - totalDistance1)
                var passSpeed1 = passSpeed / (totalDistance2 - reactionTime2)
                var passSpeed2 = speed2?.times(passSpeed1)
                passSpeedTotal = passSpeed2?.let { speed2?.minus(it) }!!
            }

            else{
                if (speed2 != null) {
                    passSpeedTotal = speed2
                }
            }


            findViewById<TextView>(R.id.firstSpeedOutputText).text = "${speed1?.toInt().toString()} km/h" //Showing up only the first two decimal numbers.
            findViewById<TextView>(R.id.secondSpeedOutputText).text = "${speed2?.toInt().toString()} km/h"
            findViewById<TextView>(R.id.firstReactionTimeOutputText).text = "${"%.2f".format(reactionTime1).toString()} s"
            findViewById<TextView>(R.id.secondReactionTimeOutputText).text = "${"%.2f".format(reactionTime2).toString()} s"
            findViewById<TextView>(R.id.firstBrakingDistanceOutputText).text = "${"%.2f".format(totalDistance1).toString()} m"
            findViewById<TextView>(R.id.secondBrakingDistanceOutputText).text = "${"%.2f".format(totalDistance2).toString()} m"
            findViewById<TextView>(R.id.conclusionOutputTextView).text = "At the point where the first motorcycle stops, the second motorcycle passes with a speed of ${"%.2f".format(passSpeedTotal).toString()} km/h."

            findViewById<TextView>(R.id.firstBrakingDistanceOutputText).setTextColor(getResources().getColor(R.color.BMWred))
            findViewById<TextView>(R.id.secondBrakingDistanceOutputText).setTextColor(getResources().getColor(R.color.BMWred))
            findViewById<TextView>(R.id.firstReactionTimeOutputText).setTextColor(getResources().getColor(R.color.BMWred))
            findViewById<TextView>(R.id.secondReactionTimeOutputText).setTextColor(getResources().getColor(R.color.BMWred))
            findViewById<TextView>(R.id.conclusionOutputTextView).setTextColor(getResources().getColor(R.color.BMWred))


        }

        else{
            findViewById<TextView>(R.id.firstSpeedOutputText).text = "km/h"
            findViewById<TextView>(R.id.secondSpeedOutputText).text = "km/h"
            findViewById<TextView>(R.id.firstBrakingDistanceOutputText).text = "km/h"
            findViewById<TextView>(R.id.firstReactionTimeOutputText).text = "m"
            findViewById<TextView>(R.id.secondReactionTimeOutputText).text = "m"
            findViewById<TextView>(R.id.conclusionOutputTextView).text = ""

            findViewById<TextView>(R.id.firstBrakingDistanceOutputText).setTextColor(getResources().getColor(R.color.black))
            findViewById<TextView>(R.id.secondBrakingDistanceOutputText).setTextColor(getResources().getColor(R.color.black))
            findViewById<TextView>(R.id.firstReactionTimeOutputText).setTextColor(getResources().getColor(R.color.black))
            findViewById<TextView>(R.id.secondReactionTimeOutputText).setTextColor(getResources().getColor(R.color.black))
            findViewById<TextView>(R.id.conclusionOutputTextView).setTextColor(getResources().getColor(R.color.black))

            v.vibrate(VibrationEffect.createOneShot(100,    //Vibrate for 100 ms
                VibrationEffect.DEFAULT_AMPLITUDE))

            Handler().postDelayed({
                v.vibrate(VibrationEffect.createOneShot(100,    //Vibrate for 100 ms
                    VibrationEffect.DEFAULT_AMPLITUDE))
            }, 300) //Delay for 300 ms
        }

        val inputManager: InputMethodManager =getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager //Automatically close keyboard.
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
    }

    fun settings(view : View){

        val intent = Intent(applicationContext,SetActivity::class.java)
        startActivity(intent)

    }
}