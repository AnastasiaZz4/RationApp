package ru.ex.dietapp.models

class RationRepository {
    companion object {
        var dateStart: Long = 0
        var dateEnd: Long = 0
        var listGoodProduct = arrayListOf<String>()
        var listBadProduct = arrayListOf<String>()
    }
}