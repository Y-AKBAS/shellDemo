package com.yakbas.shellDemo.commands

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.shell.command.CommandRegistration
import org.springframework.shell.command.annotation.Command
import org.springframework.shell.command.annotation.Option
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import org.springframework.web.util.UriComponentsBuilder

@Command
class ApiClient(private val restClient: RestClient, private val objectMapper: ObjectMapper) {

    private val httpMethods = HttpMethod.values()
    private val prettyWriter = objectMapper.writerWithDefaultPrettyPrinter()

    @Command(command = ["req"], description = "Can be used for api calls.")
    fun callIt(
        @Option(defaultValue = "GET", longNames = ["method"], shortNames = ['m']) method: String,
        @Option(required = true, longNames = ["url"], shortNames = ['u']) url: String,
        @Option(
            required = false,
            longNames = ["param"],
            shortNames = ['p'],
            arity = CommandRegistration.OptionArity.ONE_OR_MORE
        )
        params: List<String>?,
        @Option(
            required = false,
            longNames = ["header"],
            shortNames = ['h'],
            arity = CommandRegistration.OptionArity.ONE_OR_MORE
        )
        headers: List<String>?,
        @Option(required = false, longNames = ["body"], shortNames = ['b']) body: String?
    ): String {
        val httpMethod = method.uppercase().let { m -> httpMethods.find { it.matches(m) } }
        if (httpMethod == null) {
            return "No such httpMethod!"
        }

        val uri = createRequestUri(url, params)
        execute(httpMethod, uri, headers, body)
        return ""
    }

    private fun execute(method: HttpMethod, uri: String, headers: List<String>?, body: String?) {
        val reqBuilder = restClient.method(method).uri(uri)
        if (body != null) reqBuilder.body(body)

        headers?.forEach { arg ->
            val pair = arg.split("=")
            if (pair.size != 2) {
                println("Found zero or more than 1 '=' delimiter in param: $pair")
            }
            reqBuilder.headers {
                it.add(pair[0], pair[1])
            }
        }

        reqBuilder.headers {
            it.addIfAbsent(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        }

        val response = reqBuilder.retrieve().toEntity<String>()
        val cookies = response.headers.getValuesAsList(HttpHeaders.SET_COOKIE)
        val result = ApiResponse(
            status = response.statusCode.value(),
            body = getBody(response.body),
            cookies = cookies
        )
        val json = prettyWriter.writeValueAsString(result)
        println(json)
    }

    private fun createRequestUri(url: String, params: List<String>?): String {
        val builder = UriComponentsBuilder.fromHttpUrl(url)

        if (!params.isNullOrEmpty()) {
            params.forEach {
                val pair = it.split("=")
                if (pair.size != 2) {
                    println("Found zero or more than 1 '=' delimiter in param: $pair")
                }
                builder.queryParam(pair[0], pair[1])
            }
        }

        return builder.build().toUriString()
    }

    private fun getBody(responseBody: String?): JsonNode? {
        if (responseBody.isNullOrEmpty()) return null
        val tree = objectMapper.readTree(responseBody)
        return tree
    }

    private data class ApiResponse(
        val status: Int,
        val body: JsonNode?,
        val cookies: List<String>
    )
}
