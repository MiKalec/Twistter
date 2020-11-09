package br.com.twistter.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LoginController {
    @RequestMapping("/login.html")
    fun login() =
        "login.html"
}