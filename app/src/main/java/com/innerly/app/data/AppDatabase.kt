package com.innerly.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): User?
}

@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal: Goal): Long

    @Query("SELECT * FROM goals WHERE userId = :userId AND isCompleted = 0")
    suspend fun getActiveGoals(userId: Long): List<Goal>

    @Query("SELECT * FROM goals WHERE userId = :userId AND isCompleted = 1")
    suspend fun getArchivedGoals(userId: Long): List<Goal>

    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Long): Goal?

    @Update
    suspend fun update(goal: Goal)
}

@Dao
interface ReflectionDao {
    @Insert
    suspend fun insert(reflection: Reflection)

    @Query("SELECT * FROM reflections WHERE goalId = :goalId ORDER BY date DESC")
    suspend fun getReflectionsForGoal(goalId: Long): List<Reflection>
}

@Database(entities = [User::class, Goal::class, Reflection::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun goalDao(): GoalDao
    abstract fun reflectionDao(): ReflectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "innerly_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}