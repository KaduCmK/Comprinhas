package com.example.comprinhas.core.data.http

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.comprinhas.MainActivity
import com.example.comprinhas.R
import com.example.comprinhas.core.data.database.ComprinhasDatabase
import com.example.comprinhas.core.data.PreferencesRepository
import com.example.comprinhas.dataStore
import com.example.comprinhas.ui.UiState
import kotlinx.coroutines.flow.first

class SyncWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val preferencesRepository = PreferencesRepository(context.dataStore)

        val name = inputData.getString("name") ?: ""
        val idList = inputData.getIntArray("idList") ?: intArrayOf()

        Log.i("SyncWorker", "sync: $name")

        try {
            val dao = ComprinhasDatabase.getDatabase(context).shoppingItemDao()

            val onlineDatabase = DatabaseApi.retrofitService.getDatabase(
                name, idList.toList()
            ).body()!!
            Log.d("SYNCWORKER", onlineDatabase.shoppingList.toString())

            // TODO: possivel problema de notificacao sendo causado por esse trecho
            onlineDatabase.shoppingList.forEach {
                if (it !in dao.getAllShopping().first()) {
                    if (!MainActivity.isForeground) postNotification(it.nomeItem, it.adicionadoPor)
                }
            }

            preferencesRepository.updateUiState(UiState.LOADED)
            dao.clearList()
            dao.insertAll(onlineDatabase.shoppingList)

            return Result.success()
        } catch (e: Exception) {
            preferencesRepository.updateUiState(UiState.NO_CONNECTION)
            Log.e("SYNC-WORKER", e.message.toString())

            return if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
    }

    private fun postNotification(name: String, addedBy: String) {
        val builder = NotificationCompat.Builder(context, "list_notifications")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Comprinhas")
            .setContentText("$addedBy adicionou $name à lista de compras")
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(1, builder)
        }
    }
}