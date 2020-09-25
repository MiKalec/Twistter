package br.com.twistter.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TwitterplusApplication

fun main(args: Array<String>) {
    runApplication<TwitterplusApplication>(*args)
}