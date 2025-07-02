package com.example.workoutapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.workoutapp.Workout

@Database(entities = [Workout::class], version = 2)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                )
                    .addMigrations(MIGRATION_1_2) // ✅ Add this line
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 1. Create new table
        database.execSQL("""
            CREATE TABLE workouts_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                date TEXT NOT NULL,
                durationMinutes INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())

        // 2. Copy the old data
        database.execSQL("""
            INSERT INTO workouts_new (id, name, date, durationMinutes)
            SELECT id, name, date, 0
            FROM workouts
        """.trimIndent())

        // 3. Drop old table
        database.execSQL("DROP TABLE workouts")

        // 4. Rename new table
        database.execSQL("ALTER TABLE workouts_new RENAME TO workouts")
    }
}

