package com.example.instantcar.activities.utilities

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

class Dialog {
    fun showConfirmDialog(title: String, message: String, toastText: String, context: Context, action: () -> Unit) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            action()
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()

        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->}
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    fun showConfirmDialog(title: String, message: String, toastText: String, context: Context, id: String,
                          actionWithId: (id: String) -> Unit){
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            actionWithId(id)
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->}
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    fun showAlertDialog(title: String, message: String, context: Context) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton(
            "OK"
        ) { _, _ ->}

        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}