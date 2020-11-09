package br.com.twistter.repository

import br.com.twistter.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User?
    fun findByName(name: String): User?
    fun findAllByName(name: String): Iterable<User>?

    @Query("SELECT u FROM User u WHERE lower(u.name) LIKE lower(concat('%', ?1, '%')) ORDER BY u.followerCount DESC")
    fun findByNameContainingOrdered(name: String?): Iterable<User?>?

    //    TODO: Utilizar query method ao invés de @Query
    fun findByNameIgnoreCaseContaining(name: String?): Iterable<User?>?

    fun countUserFollowersById(id: Long?): Long?

    @Query(
        value = "select * from users u inner join followers f on f.follower_id = u.id where f.user_id = :userId order by u.follower_count desc",
        nativeQuery = true
    )
    fun findUsersOrderedByFollowerCount(@Param("userId") userId: Long?): List<User?>?

    @Query(
        value = "select distinct u.id from users u inner join followers f on u.id = f.follower_id " +
                "and u.id in (select follower_id from followers f inner join users u on u.id = f.user_id where u.id = :userId) ",
        nativeQuery = true
    )
    fun findFollowedUsersId(@Param("userId") userId: Long?): Collection<Long?>?

    @Query(
        value = "select * from users where login like %" + ":name" + "% and id != :userId order by follower_count desc",
        nativeQuery = true
    )
    fun findAllUsersSearchCriteria(@Param("userId") userId: Long?, @Param("name") name: String?): List<User?>?
}