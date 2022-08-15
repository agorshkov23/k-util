package ml.ialegor.util.http.feign

import com.fasterxml.jackson.databind.json.JsonMapper
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import ml.ialegor.util.http.HttpClientBuilder

inline fun <reified T> HttpClientBuilder.buildFeignClient(): T {
    val json = JsonMapper()
    return Feign.builder()
        .decoder(JacksonDecoder(json))
        .encoder(JacksonEncoder(json))
        .target(T::class.java, url)
}
