package br.com.twistter

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
open class TwistterApplication : CommandLineRunner {
    @Throws(Exception::class)
    override fun run(vararg args: String) {
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(TwistterApplication::class.java, *args)
}