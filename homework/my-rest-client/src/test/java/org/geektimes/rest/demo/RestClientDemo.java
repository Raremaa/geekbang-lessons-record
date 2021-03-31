package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class RestClientDemo {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Entity entity = Entity.text("Test Post");

        Response response = client
                .target("http://127.0.0.1:8080/hello/world?param=1")      // WebTarget
                .request() // Invocation.Builder
                .post(entity);                                 //  Response

        String content = response.readEntity(String.class);

        System.out.println(content);

    }
}
