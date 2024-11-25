package org.iesharia.composeroomapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.iesharia.composeroomapp.data.entity.TaskType

@Dao
interface TaskTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskType(taskType: TaskType)

    @Query("SELECT * FROM task_types ORDER BY id ASC")
    fun getAllTaskTypes(): Flow<List<TaskType>>

    @Query("SELECT * FROM task_types WHERE id = :taskTypeId")
    fun getTaskTypeById(taskTypeId: Int): Flow<TaskType>

    @Update
    suspend fun updateTaskType(taskType: TaskType)

    @Delete
    suspend fun deleteTaskType(taskType: TaskType)
}
