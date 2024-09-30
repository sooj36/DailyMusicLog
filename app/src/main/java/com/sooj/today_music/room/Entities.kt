package com.sooj.today_music.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId : Int =0,
    val trackName : String?,
    val artistName : String?,
    val imageUrl : String?,
)

@Entity(

    foreignKeys = [
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class MemoEntity(
    @PrimaryKey val trackId : Int,
    val memoContent : String = "오늘의 음악을 기록하세요 !",
)
