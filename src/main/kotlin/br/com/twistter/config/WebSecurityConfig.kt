package br.com.twistter.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    companion object {
        private const val LOGIN_PAGE = "/login.html"
        private const val DEFAULT_URL = "/"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(LOGIN_PAGE)
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage(LOGIN_PAGE)
            .defaultSuccessUrl(DEFAULT_URL)
            .permitAll()
            .usernameParameter(USERNAME).passwordParameter(PASSWORD).permitAll()
        http
            .headers().frameOptions().disable()
    }
}