package task1_fact_getter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FactGetter {
    private static final String REMOTE_ADDRESS =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main() {
        System.out.println("\tЗадание 1.Запрос на получение списка фактов о кошках");
        try (CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setUserAgent("HTTP_homework_program")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        ) {
            HttpGet request = new HttpGet(REMOTE_ADDRESS);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                List<Fact> facts = mapper.readValue(response.getEntity().getContent(),
                                new TypeReference<List<Fact>>() {})
                        .stream()
                        .filter(fact -> fact != null && fact.getUpvotes() > 0)
                        .collect(Collectors.toList());
                facts.forEach(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
