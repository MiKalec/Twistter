package br.com.twistter.repository

import br.com.twistter.model.Follower
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FollowerRepository : CrudRepository<Follower?, Long?> {
    fun findByUserIdAndFollowerId(userId: Long, followerId: Long): Follower?
    fun findByUserId(userId: Long): Iterable<Follower?>?

    @Query(value = "select count (user_id) from followers where user_id = :userId", nativeQuery = true)
    fun countUserFollowing(@Param("userId") userId: Long?): Int?

    @Query(value = "select count (follower_id) from followers where follower_id = :userId", nativeQuery = true)
    fun countUserFollowers(@Param("userId") userId: Long?): Int?
}