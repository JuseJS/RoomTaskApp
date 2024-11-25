package org.iesharia.composeroomapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.iesharia.composeroomapp.data.dao.TaskDao
import org.iesharia.composeroomapp.data.dao.TaskTypeDao
import org.iesharia.composeroomapp.data.entity.Task
import org.iesharia.composeroomapp.data.entity.TaskType

@Database(
    entities = [Task::class, TaskType::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskTypeDao(): TaskTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
