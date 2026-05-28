package br.com.farmacia.shared.persistence;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class JsonFileHandler {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static <T> List<T> lerArquivo(String caminho, Class<T[]> clazz) {
        File file = new File(caminho);
        if (!file.exists()) return new ArrayList<>();
        try { return new ArrayList<>(Arrays.asList(mapper.readValue(file, clazz))); }
        catch (IOException e) { return new ArrayList<>(); }
    }
    public static <T> void escreverArquivo(String caminho, List<T> dados) {
        try {
            File file = new File(caminho);
            file.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, dados);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
