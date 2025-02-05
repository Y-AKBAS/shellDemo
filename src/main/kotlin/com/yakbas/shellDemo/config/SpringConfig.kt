package com.yakbas.shellDemo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.history.DefaultHistory
import org.jline.terminal.Terminal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.boot.TerminalCustomizer
import org.springframework.shell.component.message.ShellMessageBuilder
import org.springframework.shell.component.view.TerminalUI
import org.springframework.shell.component.view.TerminalUIBuilder
import org.springframework.shell.component.view.event.KeyEvent.Key
import org.springframework.web.client.RestClient

@Configuration
class SpringConfig {

    @Bean
    fun objectMapper() = ObjectMapper().apply {
        this.registerKotlinModule()
    }

    @Bean
    fun restClient() = RestClient.create()

    @Bean
    fun terminalCustomizer(): TerminalCustomizer {
        return TerminalCustomizer {
            it.system(true)
        }
    }

    @Bean
    fun terminalUi(builder: TerminalUIBuilder, history: org.jline.reader.History): TerminalUI {
        val ui = builder.build()
        val eventLoop = ui.eventLoop
        eventLoop.keyEvents()
            .subscribe {
                if (it.key == Key.CursorUp) {
                    eventLoop.dispatch {
                        eventLoop.dispatch(ShellMessageBuilder.withPayload(history.previous()).build())
                    }
                }
            }
        return ui
    }
}
