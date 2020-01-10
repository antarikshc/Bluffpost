package com.antarikshc.bluffpost.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.antarikshc.bluffpost.models.Author
import com.antarikshc.bluffpost.models.News

@Database(entities = [News::class, Author::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao
    abstract fun authorDao(): AuthorDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun build(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // If instance is `null` make a new database instance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bluffpost_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .build()
                // Assign INSTANCE to the newly created database.
                INSTANCE = instance
                return instance
            }
        }
    }

}