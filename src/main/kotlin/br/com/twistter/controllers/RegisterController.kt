package br.com.twistter.controllers

import br.com.twistter.model.User
import br.com.twistter.model.UserRoles
import br.com.twistter.model.enums.RoleType
import br.com.twistter.repository.UserRepository
import br.com.twistter.repository.UserRolesRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Controller
class RegisterController(
    private val userRepository: UserRepository,
    private val userRolesRepository: UserRolesRepository,
    private val passwordEncoder: PasswordEncoder
) {

    companion object {
        const val SIGNUP_PAGE = "signup"
        const val REGISTER = "doRegister"
    }

    @RequestMapping(SIGNUP_PAGE)
    @Throws(IOException::class)
    fun registerPage(resp: HttpServletResponse): ModelAndView {
        val signup = ModelAndView("signup")
        val user = User()
        signup.addObject("person", user)
        return signup
    }

    @PostMapping(REGISTER)
    fun doRegister(user: @Valid User): ModelAndView {
        val mv = ModelAndView("redirect:login")
        if (!validateUser(user)) {
            mv.viewName = "signup"
            mv.addObject("person", User())
            return mv
        }
        val userSaved: User = save(user)
        mv.addObject("person", userSaved)
        return mv
    }

    private fun validateUser(user: @Valid User): Boolean {
        if (user.password != user.password2) {
            return false
        }
        return null == userRepository.findByLogin(user.login!!)
    }

    private fun save(user: User): User {
        val userToSave: User = user
        userToSave.password = passwordEncoder.encode(user.password)
        userToSave.enabled = true

        val role = UserRoles(user = userToSave, role = RoleType.USER)
        role.login = userToSave.login

        userRepository.save(userToSave)
        userRolesRepository.save(role)
        return userToSave
    }
}