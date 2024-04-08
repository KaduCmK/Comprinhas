package com.example.comprinhas.http

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.comprinhas.MainActivity
import com.example.comprinhas.R
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import com.example.comprinhas.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile

class SyncWorker(private val context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val name = inputData.getString("name") ?: ""
        val listId = inputData.getString("listId") ?: ""

        val dataStore = PreferencesRepository(context.dataStore, context)
        val localLastChanged = dataStore.preferencesFlow.first().lastChanged

        try {
            val response = DatabaseApi.retrofitService.getDatabase(name, listId)
            var onlineDatabase = response.body()!!

            val dao = ShoppingListDatabase.getDatabase(context).shoppingItemDao()

            if (onlineDatabase.lastChanged != localLastChanged) {
                Log.d("SYNC", "Datas diferentes - Iniciando sincronização...")
                val workManager = WorkManager.getInstance(context)

                val worksFlow = workManager.getWorkInfosByTagFlow("com.example.comprinhas.http.HttpWorker")
                val works = worksFlow.first()

                Log.d("SYNC-WORKER", works.toString())

                if (works.isNotEmpty()) {
                    Log.d("SYNC", "Há tarefas pendentes - enviando...")

                    var collecting = true

                    worksFlow.takeWhile { collecting }
                        .collect { list ->
                            Log.d("SYNC-WORKER", "list<workinfo> = $list")
                            if (list.all { it.state == WorkInfo.State.SUCCEEDED || it.state == WorkInfo.State.FAILED }) {
                                Log.d("SYNC", "Tasks finalizadas. Sincronizando...")
                                collecting = false

                                workManager.pruneWork()
                            }
                        }
                }
                else {
                    Log.d("SYNC", "Sem tarefas para realizar. Sincronizando...")
                }

                onlineDatabase = DatabaseApi.retrofitService.getDatabase(
                    name, listId
                ).body()!!
                onlineDatabase.shoppingList.forEach {
                    if (it !in dao.getAllShopping().first()) {
                        if (!MainActivity.isForeground) postNotification(it.name, it.addedBy)
                    }
                }
                dao.clearList()
                dao.insertAll(onlineDatabase.shoppingList)

                Log.d("SYNC-WORKER", "Novo valor de lastChanged = ${onlineDatabase.lastChanged}")
                dataStore.updateLastChanged(onlineDatabase.lastChanged)

            }
            else {
                Log.d("SYNC", "datas iguais - as bases já estão sincronizadas")
            }

            return Result.success()
        }
        catch (e: Exception) {
            Log.e("SYNC-WORKER", e.toString())
            return if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
        finally {
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