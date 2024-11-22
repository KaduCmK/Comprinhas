package com.example.comprinhas.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.comprinhas.data.shoppingItem.ShoppingItem
import com.example.comprinhas.data.shoppingItem.ShoppingItemDao
import com.example.comprinhas.home.data.ShoppingList
import com.example.comprinhas.home.data.ShoppingListDao

@Database(entities = [ShoppingList::class, ShoppingItem::class], version = 3)
abstract class ComprinhasDatabase: RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao
    abstract fun shoppingListDao(): ShoppingListDao
    companion object {
        @Volatile
        private var Instance: ComprinhasDatabase? = null

        fun getDatabase(context: Context): ComprinhasDatabase = Instance ?: synchronized(this) {
            Room
                .databaseBuilder(context, ComprinhasDatabase::class.java, "shopping_list_database")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
        }
    }
}