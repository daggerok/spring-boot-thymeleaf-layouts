package daggerok

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.LocalDateTime.*

@Configuration
class ThymeleafLayoutConfig {
  @Bean fun layoutDialect() = LayoutDialect()
}

@Configuration
class WebfluxConfig {

  @Bean
  fun routes(): RouterFunction<*> = router {
    ("/").nest {
      GET("/") {
        contentType(TEXT_HTML)
        ok().render("index")
      }
      GET("/info") {
        contentType(TEXT_HTML)
        ok().render("info", mapOf(
            "-now" to now()
        ))
      }
      GET("/api/**") {
        val map = mapOf("hello" to "world")
        ok().contentType(APPLICATION_JSON_UTF8)
            .body(Mono.just(map), map.javaClass)
      }
    } // interceptor
  }.filter { request: ServerRequest, next: HandlerFunction<ServerResponse> ->
    request.attributes().put("now", now())
    next.handle(request)
  }
}

@SpringBootApplication
class App

fun main(args: Array<String>) {
  runApplication<App>(*args)
}
