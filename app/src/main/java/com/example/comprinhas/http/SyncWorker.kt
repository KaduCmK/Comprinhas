package com.example.comprinhas.http

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
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.dataStore
import kotlinx.coroutines.flow.first

class SyncWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val name = inputData.getString("name") ?: ""
        val listId = inputData.getString("listId") ?: ""
        val listPassword = inputData.getString("listPassword") ?: "5050"

        val dataStore = PreferencesRepository(context.dataStore, context)

        try {
            val dao = ShoppingListDatabase.getDatabase(context).shoppingItemDao()

            val onlineDatabase = DatabaseApi.retrofitService.getDatabase(
                name, listId, listPassword
            ).body()!!
            Log.d("SYNCWORKER", onlineDatabase.shoppingList.toString())

            onlineDatabase.shoppingList.forEach {
                if (it !in dao.getAllShopping().first()) {
                    if (!MainActivity.isForeground) postNotification(it.nomeItem, it.adicionadoPor)
                }
            }

            dao.clearList()
            dao.insertAll(onlineDatabase.shoppingList)

            return Result.success()
        } catch (e: Exception) {
            Log.e("SYNC-WORKER", e.message.toString())
            return if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        } finally {
            dataStore.updateLoadingScreen(false)
        }
    }

    private fun postNotification(name: String, addedBy: String) {
        val builder = NotificationCompat.Builder(context, "list_notifications")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Lista de Compras")
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