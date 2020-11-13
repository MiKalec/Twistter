package br.com.twistter.controllers

import br.com.twistter.model.Follower
import br.com.twistter.model.User
import br.com.twistter.model.UserRoles
import br.com.twistter.repository.FollowerRepository
import br.com.twistter.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.security.Principal
import java.sql.Date
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class SearchControllerTest {

    private val userRepository: UserRepository = mockk()
    private val followerRepository: FollowerRepository = mockk()
    private val principal: Principal = mockk()

    private val searchController: SearchController = SearchController(userRepository, followerRepository)


    @Test
    fun searchPageTest() {
        val result = searchController.searchPage()

        Assert.assertEquals(result.viewName, "search")
    }

    @Test
    fun searchUsersTest() {
        val follower = User(
            1,
            "Follower",
            "mockuser",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        val followed = User(
            2,
            "Followed",
            "mockuser",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        every { principal.name } returns (follower.login)
        every { userRepository.findByLogin(any()) } returns (follower)
        every { userRepository.save(follower) } returns(follower)
        every { userRepository.findAllUsersSearchCriteria(follower.id, followed.name) } returns(listOf(followed))
        every { userRepository.findFollowedUsersId(follower.id) } returns(listOf(followed.id))

        val result = searchController.searchUsers(followed.name!!, principal)

        Assert.assertEquals(follower.alreadyFollows,false)
        Assert.assertEquals(result.viewName, "search")
    }

    @Test
    fun followUsersTest() {
        val follower = User(
            1,
            "Follower",
            "mockuser",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        val followed = User(
            2,
            "Followed",
            "mockfollowed",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        val followerUnit = Follower()

        every { principal.name } returns (follower.login)

        every { userRepository.findByLogin(any()) } returns (follower)
        every { userRepository.findById(1) } returns (Optional.of(follower))
        every { userRepository.findById(2) } returns (Optional.of(followed))

        every { userRepository.save(follower) } returns mockk {
            follower.followingCount = follower.followingCount!!.plus(1)
            every { userRepository.save(follower) } returns follower
        }

        every { userRepository.save(followed) } returns mockk {
            followed.followerCount = followed.followerCount!!.plus(1)
            every { userRepository.save(followed) } returns followed
        }

        every { userRepository.findAllUsersSearchCriteria(follower.id, followed.name) } returns(listOf(followed))
        every { userRepository.findFollowedUsersId(follower.id) } returns(listOf(followed.id))

        every { followerRepository.save(any<Follower>()) } returns Follower()
        every { followerRepository.countUserFollowers(any()) } returns 1
        every { followerRepository.countUserFollowing(any()) } returns 1

        val result = searchController.followUser(principal, followed.id!!)

        Assert.assertEquals(follower.followingCount,1)
        Assert.assertEquals(followed.followerCount,1)
        Assert.assertEquals(result.viewName, "redirect:/search")
    }

    @Test
    fun unfollowUsersTest() {
        val follower = User(
            1,
            "Follower",
            "mockuser",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        val followed = User(
            2,
            "Followed",
            "mockfollowed",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        val followerUnit = Follower()
        followerUnit.followerId = 1

        every { principal.name } returns (follower.login)

        every { userRepository.findByLogin(any()) } returns (follower)
        every { userRepository.findById(1) } returns (Optional.of(follower))
        every { userRepository.findById(2) } returns (Optional.of(followed))

        every { userRepository.save(follower) } returns mockk {
            follower.followingCount = follower.followingCount!!.minus(1)
            every { userRepository.save(follower) } returns follower
        }

        every { userRepository.save(followed) } returns mockk {
            followed.followerCount = followed.followerCount!!.minus(1)
            every { userRepository.save(followed) } returns followed
        }

        every { userRepository.findAllUsersSearchCriteria(follower.id, followed.name) } returns(listOf(followed))
        every { userRepository.findFollowedUsersId(follower.id) } returns(listOf(followed.id))

        every { followerRepository.save(any<Follower>()) } returns followerUnit
        every { followerRepository.deleteById(any()) } returns Unit
        every { followerRepository.findByUserIdAndFollowerId(follower.id!!, followed.id!!) } returns followerUnit
        every { followerRepository.countUserFollowers(followed.id) } returns 0
        every { followerRepository.countUserFollowing(follower.id) } returns 0

        val result = searchController.unfollowUser(principal, followed.id!!)

        Assert.assertEquals(follower.followingCount,0)
        Assert.assertEquals(followed.followerCount,0)
        Assert.assertEquals(result.viewName, "redirect:/search")
    }

}