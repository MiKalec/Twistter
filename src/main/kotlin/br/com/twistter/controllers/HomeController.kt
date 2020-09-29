package br.com.twistter.controllers

import br.com.twistter.model.User
import br.com.twistter.repository.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.security.Principal

@Controller
class HomeController(
    private val userRepository: UserRepository
) {

    @RequestMapping("/home.html")
    fun homePage(principal: Principal): ModelAndView {
        var mv = ModelAndView("home")
        val user = getUserLogged(principal)

        return ModelAndView("home")
    }

    private fun getUserLogged(principal: Principal) =
        userRepository.findByLogin(principal.name)

}