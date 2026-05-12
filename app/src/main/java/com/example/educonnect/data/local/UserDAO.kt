package com.example.educonnect.data.local

import androidx.room.*

@Dao
interface UserDAO {
    //Fungsi untuk menyimpan data pendaftaran mahasiswa
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    //Fungsi untuk mengambil data profil yang sudah tersimpan
    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserEntity?

    //Fungsi dummy untuk menghapus data jika ingin logout/reset
    @Query("DELETE FROM user_profile")
    suspend fun clearData()
}