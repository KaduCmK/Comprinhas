package com.example.comprinhas.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.comprinhas.http.HttpWorker
import com.example.comprinhas.http.WorkerOperation
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

class ShoppingListRepository(private val dao: ShoppingItemDao, private val context: Context) {

    val shoppingList: Flow<List<ShoppingItem>> = dao.getAllShopping()
    val cartList: Flow<List<ShoppingItem>> = dao.getAllCart()

    private fun buildHttpWorkRequest(
        operation: Int,
        item: ShoppingItem? = null,
        idList: List<Long> = emptyList(),
        lastChanged: Long = ZonedDateTime.now().toEpochSecond()
    ): OneTimeWorkRequest {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = Data.Builder()
            .putInt("workerOperation", operation)
            .putLong("id", item?.id ?: -1)
            .putString("name", item?.name)
            .putString("addedBy", item?.addedBy)
            .putLongArray("idList", idList.toLongArray())
            .putLong("lastChanged", lastChanged)
            .build()

        return OneTimeWorkRequest.Builder(HttpWorker::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
    }

    suspend fun insert(item: ShoppingItem) {
        dao.insert(item)

        val insertWorker = buildHttpWorkRequest(WorkerOperation.INSERT, item)
        WorkManager.getInstance(context).enqueue(insertWorker)
    }

    suspend fun deleteFromList(item: ShoppingItem, lastChanged: Long) {
        dao.deleteFromList(item)

        val deleteWorker = buildHttpWorkRequest(WorkerOperation.DELETE_FROM_LIST, item, lastChanged = lastChanged)
        WorkManager.getInstance(context).enqueue(deleteWorker)
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

    suspend fun clearCart(idList: List<Long>, lastChanged: Long) {
        dao.clearCart()

        val clearListWorker = buildHttpWorkRequest(
            WorkerOperation.CLEAR_CART,
            idList = idList,
            lastChanged = lastChanged
        )

        WorkManager.getInstance(context).enqueue(clearListWorker)
    }

}