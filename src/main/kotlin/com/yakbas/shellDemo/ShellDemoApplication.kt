package com.yakbas.shellDemo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.shell.command.annotation.CommandScan

@SpringBootApplication
@CommandScan
class ShellDemoApplication

fun main(args: Array<String>) {
    runApplication<ShellDemoApplication>(*args)
}
