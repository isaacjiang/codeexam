package com.qohash.codeexam.configration;

import com.qohash.codeexam.microservices.WebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/***
 * Here config all api urls
 */

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> SystemRouterFunction(WebService webService) {
        return nest(path("/api"), route(RequestPredicates.GET("/file_list").and(accept(MediaType.APPLICATION_JSON)), webService::getFileList)
        );
    }

}

