package server.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Task;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapters extends TypeAdapter<LocalDateTime> {
    protected transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.value("*");
        } else {
            jsonWriter.value(localDateTime.format(formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        try {
            String input = jsonReader.nextString();
            if (input == null) {
                return null;
            }
            LocalDateTime localDateTime = LocalDateTime.parse(input, formatter);
            return LocalDateTime.from(localDateTime);
        } catch (DateTimeException ex) {
            System.out.println("Не удалось прочитать localDateTime");
            return null;
        }
    }
}
