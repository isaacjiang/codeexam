package com.qohash.codeexam.microservices;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Optional;


@Service
public class WebService {

    @Autowired
    private FileService fileService;

    public WebService() {
    }

    public Mono<ServerResponse> getFileList(ServerRequest request) {
        Optional<String> rootFolder = request.queryParam("root");
        System.out.println(rootFolder);

        JSONObject output = fileService.getFoldersAndFilesList(rootFolder.get());

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(output.toString()), String.class);
    }
}
