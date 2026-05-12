package com.example.educonnect.utils

object NIMMapper {
    fun getInfoFromNIM(nim: String): String {
        return when{
            nim.length < 5 -> ""
            nim.contains("2443") -> "TRPL"
            nim.contains("2442") -> "TI"
            else -> "Prodi Tidak Dikenal"
        }
    }
}