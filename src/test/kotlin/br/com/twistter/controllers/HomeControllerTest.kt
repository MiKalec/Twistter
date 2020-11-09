package br.com.twistter.controllers

import br.com.twistter.model.User
import br.com.twistter.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.security.Principal

class HomeControllerTest {

    val userRepository: UserRepository = mockk()
    val principal: Principal = mockk()

    val homeController: HomeController = HomeController(userRepository)

    @Test
    fun testHomeController() {
        every { principal.name } returns "mockUser"
        every { userRepository.findByLogin(any()) } returns User()

        val result = homeController.homePage(principal)

        Assert.assertEquals(result.viewName, "home")
    }
}