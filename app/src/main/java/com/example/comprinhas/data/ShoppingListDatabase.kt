package com.example.comprinhas.data

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [ShoppingItem::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
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
    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

}