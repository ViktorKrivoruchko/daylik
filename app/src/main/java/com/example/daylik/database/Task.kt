package com.example.daylik.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    var isCompleted: Boolean,
    val isTemporary: Boolean,
    val lastReset: Long = System.currentTimeMillis()
)