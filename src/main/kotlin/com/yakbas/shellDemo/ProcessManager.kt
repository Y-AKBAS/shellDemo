package com.yakbas.shellDemo

import org.jline.terminal.Terminal
import org.springframework.stereotype.Component

@Component
class ProcessManager (private val terminal: Terminal){

    fun runProcessWithArgs(vararg args: String){
        val process = ProcessBuilder().command(*args).start()
        val inputReader = process.inputReader()
        val errorReader = process.errorReader()
        val result = process.waitFor()
        val terminalWriter = terminal.writer()
        val text = if (result == 0) {
            inputReader.readText()
        } else {
            errorReader.readText()
        }
        terminalWriter.write(text)
        terminalWriter.flush()
    }
}