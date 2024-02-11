package edu.tum.ase.ui;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Component
public class RequestForwarder {

    private final RestTemplate restTemplate;

    @Autowired
    public RequestForwarder(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void forward(HttpServletRequest request, HttpServletResponse response, String serviceUrl, String username) {
        try {
            // Extract request body, headers, method, and create HttpEntity
            byte[] body = request.getInputStream().readAllBytes();
            HttpHeaders headers = extractHeaders(request);
            headers.add("Username", username);
            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);

            // Forward the request to the service URL
            ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, method, httpEntity, String.class);

            // Set the response status, headers, and body
            response.setStatus(responseEntity.getStatusCodeValue());
            MediaType contentType = responseEntity.getHeaders().getContentType();
            if (contentType!=null) {
                response.setContentType(contentType.toString());
            }
            response.getOutputStream().write(responseEntity.getBody().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> headers.addAll(headerName, Collections.list(request.getHeaders(headerName))));
        return headers;
    }
}
