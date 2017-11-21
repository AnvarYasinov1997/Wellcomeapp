package com.mistreckless.support.wellcomeapp.ui.view.camera

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.NumberPicker
import java.io.Serializable

/**
 * Created by mistreckless on 10.10.17.
 */

class TimePickerDialog : DialogFragment() {


    @Suppress("UNCHECKED_CAST")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sayYes = arguments?.getSerializable("yes") as (Int, Int)->Unit
        val dialog =TimePickerDialog(context, TimePickerDialog.OnTimeSetListener{ timePicker, h, m -> sayYes(h,m) },10,25,true)
        dialog.setTitle("Время окончания")
        return  dialog
    }
    companion object {
        fun newInstance(sayYes: (h: Int,m: Int) -> Unit) : com.mistreckless.support.wellcomeapp.ui.view.camera.TimePickerDialog{
            val instance = TimePickerDialog()
            instance.arguments=Bundle().apply {
                putSerializable("yes",sayYes as Serializable)
            }
            return instance
        }
    }
}

class AgePickerDialog : DialogFragment() {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val sayYes = arguments?.getSerializable("yes") as (Int)->Unit
        val sayNo = arguments?.getSerializable("no") as () -> Unit

        val numberPicker = NumberPicker(context)
        numberPicker.apply {
            minValue = 0
            maxValue = 40
            value = 18
        }
        return AlertDialog.Builder(context)
                .setView(numberPicker)
                .setPositiveButton("Ok", { d, _ ->
                    sayYes(numberPicker.value)
                })
                .setNegativeButton("Cancel", { d, _ ->
                    sayNo()
                })
                .setCancelable(false)
                .create()
    }

    companion object {

        fun newInstance(sayYes: (age: Int) -> Unit , sayNo: () -> Unit): AgePickerDialog {
            val instance = AgePickerDialog()
            instance.arguments=Bundle().apply {
                putSerializable("yes", sayYes as Serializable)
                putSerializable("no",sayNo as Serializable)
            }
            return instance
        }
    }
}