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

    @RequestMapping("/")
    fun homePage(principal: Principal): ModelAndView {
        val mv = ModelAndView("home")
        val user = getUserLogged(principal)
        fillUser(mv, user)

        return mv
    }

    private fun fillUser(mv: ModelAndView, user: User?) {
        mv.addObject("user", user)
    }

    private fun getUserLogged(principal: Principal) =
        userRepository.findByLogin(principal.name)

}