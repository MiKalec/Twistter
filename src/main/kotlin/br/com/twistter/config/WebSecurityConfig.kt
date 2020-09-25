package br.com.twistter.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    companion object {
        private const val LOGIN_PAGE = "/login.html"
        private const val DEFAULT_URL = "/"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    @Autowired
    lateinit var dataSource: DataSource

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

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select login,password,enabled from users where login=?")
            .authoritiesByUsernameQuery("select login, role from user_roles where login=?")
            .passwordEncoder(BCryptPasswordEncoder())
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}