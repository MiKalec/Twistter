package br.com.twistter.controllers

import br.com.twistter.model.User
import br.com.twistter.model.UserRoles
import br.com.twistter.repository.UserRepository
import br.com.twistter.repository.UserRolesRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.sql.Date
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.servlet.http.HttpServletResponse

class RegisterControllerTest {

    private val userRepository: UserRepository = mockk()
    private val userRolesRepository: UserRolesRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    private val httpServletResponse: HttpServletResponse = mockk()

    private val registerController: RegisterController = RegisterController(userRepository, userRolesRepository, passwordEncoder)

    @Test
    fun registerPageTest() {
        val result = registerController.registerPage(httpServletResponse)

        Assert.assertEquals(result.viewName, "signup")
    }

    @Test
    fun doRegisterTest() {
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
        every { userRolesRepository.save(UserRoles()) } returns(UserRoles())
        every { userRepository.findByLogin(user.login!!) } returns(user)

        val result = registerController.doRegister(user)

        Assert.assertEquals(result.viewName, "signup")
    }
}