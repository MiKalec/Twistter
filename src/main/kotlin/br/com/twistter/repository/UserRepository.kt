package br.com.twistter.repository

import br.com.twistter.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User?
    fun findByName(name: String): User?
    fun findAllByName(name: String): Iterable<User>?
}