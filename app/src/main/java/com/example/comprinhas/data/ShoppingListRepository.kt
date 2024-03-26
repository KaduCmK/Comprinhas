package com.example.comprinhas.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import com.example.comprinhas.http.DatabaseApi
import com.example.comprinhas.http.HttpWorker
import com.example.comprinhas.http.WorkerOperation
import kotlinx.coroutines.flow.Flow

class ShoppingListRepository(private val dao: ShoppingItemDao, val context: Context) {

    val retrofitService = DatabaseApi.retrofitService

    val shoppingList: Flow<List<ShoppingItem>> = dao.getAllShopping()
    val cartList: Flow<List<ShoppingItem>> = dao.getAllCart()

    val pendingWorks: MutableList<OneTimeWorkRequest> = mutableListOf()

    private fun buildHttpWorkRequest(operation: Int): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = Data.Builder()
            .putInt("workerOperation", operation)
            .build()

        return OneTimeWorkRequest.Builder(HttpWorker::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
    }

    suspend fun insert(item: ShoppingItem, internet: Boolean) {
        dao.insert(item)

        val insertWorker = buildHttpWorkRequest(WorkerOperation.INSERT)
        if (internet) WorkManager.getInstance(context).enqueue(insertWorker)
        else pendingWorks.add(insertWorker)

    }

    suspend fun deleteFromList(item: ShoppingItem, internet: Boolean = false) {
        dao.deleteFromList(item)

        val deleteWorker = buildHttpWorkRequest(WorkerOperation.DELETE_FROM_LIST)
        if (internet) WorkManager.getInstance(context).enqueue(deleteWorker)
        else pendingWorks.add(deleteWorker)
    }

    suspend fun clearList() {
        dao.clearList()
    }

    suspend fun moveToCart(id: Long) {
        dao.moveToCart(id)
    }

    suspend fun removeFromCart(id: Long) {
        dao.removeFromCart(id)
    }

    suspend fun clearCart(internet: Boolean = false) {
        dao.clearCart()

        val clearListWorker = buildHttpWorkRequest(WorkerOperation.CLEAR_CART)
        if (internet) WorkManager.getInstance(context).enqueue(clearListWorker)
        else pendingWorks.add(clearListWorker)
    }

}