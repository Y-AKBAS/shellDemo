package com.yakbas.shellDemo.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.yakbas.shellDemo.common.ProcessManager
import org.springframework.shell.command.CommandRegistration
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option

@Command
class GitCommand(private val processManager: ProcessManager, private val objectMapper: ObjectMapper) {

    @Command(command = ["git"], description = "A git interface. You can run any git command with this.")
    fun gitIt(@Option(arity = CommandRegistration.OptionArity.ONE_OR_MORE) list: List<String>) {
        processManager.runProcessWithArgs("git", *list.toTypedArray())
    }




}