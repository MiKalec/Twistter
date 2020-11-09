package br.com.twistter.repository

import br.com.twistter.model.Tweet
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TweetRepository : CrudRepository<Tweet, Long> {
    @Query(
        value = "select t.* from tweets t inner join followers f on t.user_id = f.follower_id where f.user_id = :userId " +
                "union select * from tweets where user_id = :userId order by creation_time desc", nativeQuery = true
    )
    fun findTweets(@Param("userId") userId: Long?): Collection<Tweet?>?

    @Query(value = "select count (user_id) from tweets where user_id = :userId", nativeQuery = true)
    fun countTweets(@Param("userId") userId: Long?): Int?
}
