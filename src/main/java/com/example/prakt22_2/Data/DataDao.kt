package com.example.prakt22_2.Data

import androidx.room.*

@Dao
interface DataDao {
    @Query("SELECT * FROM data_table WHERE name = :name LIMIT 1")
    suspend fun getCharacterByName(name: String): DataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: DataEntity)

    @Update
    suspend fun updateData(data: DataEntity)

    @Delete
    suspend fun deleteData(data: DataEntity)

    @Query("SELECT * FROM data_table")
    suspend fun getAllData(): List<DataEntity>
}
