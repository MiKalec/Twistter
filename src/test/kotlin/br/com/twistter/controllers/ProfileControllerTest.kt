package br.com.twistter.controllers

import br.com.twistter.model.User
import br.com.twistter.model.UserRoles
import br.com.twistter.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.security.Principal
import java.sql.Date
import java.time.LocalDateTime
import java.time.ZoneOffset

class ProfileControllerTest {
    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val principal: Principal = mockk()

    private val profileController = ProfileController(userRepository, passwordEncoder)

    @Test
    fun testaProfileController(){
        every { principal.name }.returns("mockUser")
        every { userRepository.findByLogin(any()) } returns User(id = 1)
        val result = profileController.profilePage(principal)

        Assert.assertEquals(result.viewName, "profile")
    }

    @Test
    fun testaChangePassSuccess(){
        val user = User(
            1,
            "MockUser",
            "mockuser",
            "mockpass",
            "mockpass",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        every { userRepository.save(user) } returns(user)
        every { userRepository.findByLogin(user.login!!) } returns(user)
        every { passwordEncoder.encode(any()) }.returns("encodedPass")

        val result = profileController.alterProfile(user)

        Assert.assertEquals(result.viewName, "redirect:/")
    }

    @Test
    fun testaChangePassFail(){
        val user = User(
            1,
            "MockUser",
            "mockuser",
            "mockpass",
            "mockpassssssss",
            "M",
            true,
            Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"))),
            setOf(UserRoles())
        )

        every { userRepository.findByLogin(user.login!!) } returns(user)

        val result = profileController.alterProfile(user)

        Assert.assertEquals(result.viewName, "profile")
    }
}