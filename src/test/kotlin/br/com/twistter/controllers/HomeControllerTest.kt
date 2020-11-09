package br.com.twistter.controllers

import br.com.twistter.model.Tweet
import br.com.twistter.model.User
import br.com.twistter.repository.TweetRepository
import br.com.twistter.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.security.Principal

class HomeControllerTest {

    private val userRepository: UserRepository = mockk()
    private val tweetRepository: TweetRepository = mockk()
    private val principal: Principal = mockk()
    private val tweet: Tweet = mockk()

    private val homeController: HomeController = HomeController(userRepository, tweetRepository)

    @Test
    fun testHomeController() {
        every { principal.name } returns "mockUser"
        every { userRepository.findByLogin(any()) } returns User()
        every { tweetRepository.findTweets(any()) } returns null

        val result = homeController.homePage(tweet, principal)

        Assert.assertEquals(result.viewName, "home")
    }
}