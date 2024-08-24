package com.yakbas.shellDemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.shell.command.annotation.CommandScan

@SpringBootApplication
@CommandScan
class ShellDemoApplication

fun main(args: Array<String>) {
	runApplication<ShellDemoApplication>(*args)
}
