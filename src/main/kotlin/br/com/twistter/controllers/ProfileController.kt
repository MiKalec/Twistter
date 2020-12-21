package br.com.twistter.controllers

import br.com.twistter.model.User
import br.com.twistter.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.security.Principal
import javax.validation.Valid

@Controller
class ProfileController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @RequestMapping("/profile")
    fun profilePage(user: Principal): ModelAndView {
        val profile = ModelAndView("profile")

        val userToAlter = getUserFromSession(user.name)
        userToAlter.password = ""
        profile.addObject("person", userToAlter)

        return profile
    }

    @PostMapping("/alterProfile")
    fun alterProfile(user: @Valid User): ModelAndView {
        val mv = ModelAndView("redirect:/")
        val userToAlter: User = getUserFromSession(user.login!!)
        setNewUserData(user, userToAlter)
        userRepository.save(userToAlter)
        mv.addObject("person", userToAlter)
        return mv
    }

    private fun getUserFromSession(login: String): User =
        userRepository.findByLogin(login)!!

    private fun setNewUserData(user: @Valid User, userToAlter: User) {
        userToAlter.login = user.login
        userToAlter.name = user.name
        userToAlter.gender = user.gender
        userToAlter.password = passwordEncoder.encode(user.password)
    }
}