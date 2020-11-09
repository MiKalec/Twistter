package br.com.twistter.utils

object StringHelper {
    fun formatDisplayTime(time: Long): String {
        val minutes = (time / (1000 * 60) % 60).toInt()
        val hours = (time / (1000 * 60 * 60) % 24).toInt()
        val days = (time / (1000 * 60 * 60 * 24) % 7).toInt()

        return when {
            days > 1 -> {
                "$days dias atrás"
            }
            days == 1 -> {
                "$days dia atrás"
            }
            hours > 1 -> {
                "$hours horas atrás"
            }
            hours == 1 -> {
                "$hours hora atrás"
            }
            minutes > 1 -> {
                "$minutes minutos atrás"
            }
            minutes == 1 -> {
                "$minutes minuto atrás"
            }
            else -> {
                "Agora"
            }
        }
    }
}
