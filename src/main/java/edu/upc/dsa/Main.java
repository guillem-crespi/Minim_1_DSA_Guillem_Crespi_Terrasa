package edu.upc.dsa;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // URL base que el servidor HTTP Grizzly escoltarà
    public static final String BASE_URI = "http://localhost:8080/dsaApp/";

    /**
     * Inicia el servidor HTTP Grizzly exposant els serveis JAX-RS del paquet especificat.
     * @return Servidor HTTP Grizzly.
     */
    public static HttpServer startServer() {
        // Escaneja els recursos i proveïdors del paquet de serveis REST
        final ResourceConfig rc = new ResourceConfig().packages("edu.upc.dsa.services");

        // Crea i inicia el servidor http grizzly exposant l'aplicació Jersey a BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Mètode principal que arrenca el servidor.
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s\nHit enter to stop it...", BASE_URI));

        System.in.read();
        server.stop();
    }
}