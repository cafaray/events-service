package main.kotlin.com.supplier.championleague.service

import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.repositories.SongRepository

@ApplicationScoped
class SongService (val songRepository: SongRepository) {

    fun getSongs(): ArrayList<Map<String, Any>>?{
        return songRepository.getSongs()
    }

    fun getSong(uid: String): Map<String, Any>? {
        return songRepository.getSong(uid)
    }

}