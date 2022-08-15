package io.github.ialegor.util.http

class HttpClientBuilder {
    lateinit var url: String

    fun url(url: String): HttpClientBuilder {
        this.url = url
        return this
    }
}
