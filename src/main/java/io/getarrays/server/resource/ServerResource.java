package io.getarrays.server.resource;

import io.getarrays.server.enumeration.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.service.ServerService;
import io.getarrays.server.service.ServerServiceImpl;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import io.getarrays.server.model.response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerServiceImpl serverService;

    @GetMapping("/list")
    public ResponseEntity<response> getServers(){
        return ResponseEntity.ok(
                response.builder()
                        .timestamp(now())
                        .data(of("servers", serverService.list(30)))
                        .message("Servers retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );


    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<response> PingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server= serverService.ping(ipAddress);
        return ResponseEntity.ok(
                response.builder()
                        .timestamp(now())
                        .data(of("server", server))
                        .message(server.getStatus() == Status.SERVER_UP ? "Ping Success" : "Ping Failed")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );


    }

    @PostMapping("/save")
    public ResponseEntity<response> PingServer(@RequestBody @Valid Server server) throws IOException {
        /*The @Valid annotation ensures the validation of the whole object. Importantly, it performs the validation of
        the whole object graph. However, this creates issues for scenarios needing only partial validation. On the other hand,
        we can use @Validated for group validation, including the above partial validation. dans cet exemple on vas checker dans la class
        Server cette validation :     @NotEmpty(message = "Ip address can not be empty or null! ")*/
        return ResponseEntity.ok(
                response.builder()
                        .timestamp(now())
                        .data(of("server", serverService.create(server)))
                        .message("Server created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );


    }

    @GetMapping("/get/{id}")
    public ResponseEntity<response> GetServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                response.builder()
                        .timestamp(now())
                        .data(of("server", serverService.get(id)))
                        .message("Server Retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<response> DeleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                response.builder()
                        .timestamp(now())
                        .data(of("deleted", serverService.delete(id)))
                        .message("Server Deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );


    }

    @GetMapping(path ="/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] GetServerImageS(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get("src","main","resources","images/" + fileName));
    }
}
