package io.github.kaducmk.comprinhas.core.data.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import io.github.kaducmk.comprinhas.list.data.model.ShoppingItem
//import io.github.kaducmk.comprinhas.list.data.model.ShoppingItemDao
//import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
//import io.github.kaducmk.comprinhas.home.data.ShoppingListDao
//
//@Database(entities = [ShoppingList::class, ShoppingItem::class], version = 3)
//abstract class ComprinhasDatabase: RoomDatabase() {
//
//    abstract fun shoppingItemDao(): ShoppingItemDao
//    abstract fun shoppingListDao(): ShoppingListDao
//    companion object {
//        @Volatile
//        private var Instance: ComprinhasDatabase? = null
//
//        fun getDatabase(context: Context): ComprinhasDatabase = Instance ?: synchronized(this) {
//            Room
//                .databaseBuilder(context, ComprinhasDatabase::class.java, "shopping_list_database")
//                .fallbackToDestructiveMigration()
//                .build()
//                .also { Instance = it }
//        }
//    }
//}