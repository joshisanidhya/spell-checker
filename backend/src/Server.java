import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Server {

    private final SpellChecker spellChecker;

    public Server(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    public void start() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port),
                0
        );

        // A single root handler routes exact paths; static files are allow-listed below.
        server.createContext("/", this::route);
        server.setExecutor(null);
        server.start();

        System.out.println("================================");
        System.out.println("       ShuddhAI Backend");
        System.out.println("================================");
        System.out.println("Server listening on 0.0.0.0:" + port);
    }

    private void route(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (path) {
            case "/" -> serveStatic(exchange, method, "index.html", "text/html; charset=UTF-8");
            case "/style.css" -> serveStatic(exchange, method, "style.css", "text/css; charset=UTF-8");
            case "/script.js" -> serveStatic(exchange, method, "script.js", "application/javascript; charset=UTF-8");
            case "/health" -> {
                if (method.equalsIgnoreCase("GET")) {
                    sendResponse(exchange, "{\"status\":\"ok\"}", 200);
                } else {
                    sendResponse(exchange, "{\"error\":\"Only GET requests are allowed.\"}", 405);
                }
            }
            case "/check" -> handleCheck(exchange);
            default -> sendResponse(exchange, "Not Found", 404, "text/plain; charset=UTF-8");
        }
    }

    private void serveStatic(
            HttpExchange exchange,
            String method,
            String fileName,
            String contentType
    ) throws IOException {
        if (!method.equalsIgnoreCase("GET")) {
            sendResponse(exchange, "Method Not Allowed", 405, "text/plain; charset=UTF-8");
            return;
        }

        Path frontendRoot = Path.of("frontend").toAbsolutePath().normalize();
        Path file = frontendRoot.resolve(fileName).normalize();
        if (!file.startsWith(frontendRoot) || !Files.isRegularFile(file)) {
            sendResponse(exchange, "Not Found", 404, "text/plain; charset=UTF-8");
            return;
        }

        byte[] content = Files.readAllBytes(file);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, content.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(content);
        }
    }

    private void handleCheck(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, "{\"error\":\"Only POST requests are allowed.\"}", 405);
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String word = extractWord(body);
        if (word == null || word.trim().isEmpty()) {
            sendResponse(exchange, "{\"error\":\"Please enter a Hindi word.\"}", 400);
            return;
        }

        word = Tokenizer.cleanText(word);
        if (word.isEmpty()) {
            sendResponse(exchange, "{\"error\":\"Please enter a valid Hindi word.\"}", 400);
            return;
        }
        if (word.codePointCount(0, word.length()) < 3) {
            sendResponse(exchange, "{\"error\":\"Please enter a Hindi word with at least 3 characters.\"}", 400);
            return;
        }

        boolean correct = spellChecker.isKnownWord(word);
        List<String> suggestions = correct ? List.of() : spellChecker.getSuggestions(word, 5);
        StringBuilder json = new StringBuilder();
        json.append("{\"word\":\"").append(escapeJson(word)).append("\",");
        json.append("\"correct\":").append(correct).append(",\"suggestions\":[");
        for (int i = 0; i < suggestions.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(escapeJson(suggestions.get(i))).append("\"");
        }
        json.append("]}");
        sendResponse(exchange, json.toString(), 200);
    }

    private String extractWord(String body) {
        String key = "\"word\"";
        int keyIndex = body.indexOf(key);
        if (keyIndex == -1) return null;
        int colonIndex = body.indexOf(":", keyIndex + key.length());
        if (colonIndex == -1) return null;
        int firstQuote = body.indexOf("\"", colonIndex + 1);
        if (firstQuote == -1) return null;
        int secondQuote = body.indexOf("\"", firstQuote + 1);
        if (secondQuote == -1) return null;
        return body.substring(firstQuote + 1, secondQuote);
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void sendResponse(HttpExchange exchange, String response, int status) throws IOException {
        sendResponse(exchange, response, status, "application/json; charset=UTF-8");
    }

    private void sendResponse(HttpExchange exchange, String response, int status, String contentType) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
