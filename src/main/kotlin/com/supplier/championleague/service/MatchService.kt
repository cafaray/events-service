package main.kotlin.com.supplier.championleague.service

import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.repositories.MatchRepository

@ApplicationScoped
class MatchService (val matchRepository: MatchRepository) {

    fun getMatches(): ArrayList<Map<String, Any>>?{
        return matchRepository.getMatches()
    }

    fun getMatch(uid: String): Map<String, Any>? {
        return matchRepository.getMatch(uid)
    }

    fun getMatchDetails(uid: String): Map<String, Any>? {
        return matchRepository.getMatchDetailed(uid)
    }

}