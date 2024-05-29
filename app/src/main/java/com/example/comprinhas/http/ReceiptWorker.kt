package com.example.comprinhas.http

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.comprinhas.R
import com.google.gson.Gson

class ReceiptWorker(private val context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params)
{
    override suspend fun doWork(): Result {
        val retrofitService = DatabaseApi.retrofitService

        val username = inputData.getString("username") ?: ""
        val url = inputData.getString("url") ?: ""

        Log.d("ReceiptWorker", "username: $username, url: $url")
        val output = Data.Builder()

        receiptNotification(loading = true, null)
        try {
            val response = retrofitService.newReceipt(BodyRequest(username, url))

            // enviando o código http p viewmodel ler
            output.putInt("response_code", response.code())

            if (response.code() == 200) {

                NotificationManagerCompat.from(context).cancel(2)

                return Result.success(output.build())
            }
            else {
                val message = Gson().fromJson(response.errorBody()!!.charStream(), ReceiptResponse::class.java)
                Log.i("ReceiptWorker", "${response.code()} -- ${message.error} ")

                receiptNotification(false, message.error)

                return Result.failure(output.build())
            }
        }
        catch (e: Exception) {
            Log.e("ReceiptWorker", "Error: $e")
            receiptNotification(false, "Erro interno")

            return Result.failure(output.build())
        }
    }

    private fun receiptNotification(loading: Boolean, error: String?) {
        val builder = NotificationCompat.Builder(context, "list_notifications").apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(
                if (loading) "Enviando nota fiscal..." else "Não foi possível enviar a nota fiscal"
            )
            if (!error.isNullOrEmpty()) {
                setContentText(error)
            }
            setProgress(0, 0, loading)
            setOngoing(loading)
        }

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(2, builder.build())
        }
    }
}