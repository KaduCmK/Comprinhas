package com.example.comprinhas.http

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.work.CoroutineWorker
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import com.example.comprinhas.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile

class SyncWorker(private val context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dataStore = PreferencesRepository(context.dataStore, context)
        val localLastChanged = dataStore.preferencesFlow.first().lastChanged

        try {
            val response = DatabaseApi.retrofitService.getDatabase()
            var onlineDatabase = response.body()!!

            val dao = ShoppingListDatabase.getDatabase(context).shoppingItemDao()
            val localRepo = ShoppingListRepository(dao, context)

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

                                onlineDatabase = DatabaseApi.retrofitService.getDatabase().body()!!
                                localRepo.clearList()
                                onlineDatabase.shoppingList.forEach { dao.insert(it) }
                            }
                        }
                }
                else {
                    Log.d("SYNC", "Sem tarefas para realizar. Sincronizando...")
                    onlineDatabase = DatabaseApi.retrofitService.getDatabase().body()!!
                    localRepo.clearList()
                    onlineDatabase.shoppingList.forEach { dao.insert(it) }
                }

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
            Toast.makeText(context, e.cause.toString(), Toast.LENGTH_LONG).show()
            return if (this.runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
        finally {
            dataStore.updateLoadingScreen(false)
        }
    }
}