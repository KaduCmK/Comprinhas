package com.example.comprinhas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 2)
abstract class ShoppingListDatabase: RoomDatabase() {

    abstract fun shoppingItemDao(): ShoppingItemDao
    companion object {
        @Volatile
        private var Instance: ShoppingListDatabase? = null

        fun getDatabase(context: Context): ShoppingListDatabase = Instance?: synchronized(this) {
            Room
                .databaseBuilder(context, ShoppingListDatabase::class.java, "shopping_list_database")
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
        }
    }
}