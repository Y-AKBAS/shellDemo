package com.yakbas.shellDemo

import org.springframework.shell.command.CommandRegistration
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command
class FirstCommand(private val processManager: ProcessManager) {

    @Command(command = ["git"])
    fun printIt(@Option(arity = CommandRegistration.OptionArity.ONE_OR_MORE) list: List<String>) {
        processManager.runProcessWithArgs("git", *list.toTypedArray())
    }
}