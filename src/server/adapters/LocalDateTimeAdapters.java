package server.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
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
        System.out.println("зашел в write localTime");
        if (localDateTime == null) {
            System.out.println("localTime = null");
            jsonWriter.nullValue();
            System.out.println("Записал что localTime = null");
        } else {
            jsonWriter.value(localDateTime.format(formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        System.out.println("зашел в reader localTime");
        try {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            String input = jsonReader.nextString();
            LocalDateTime localDateTime = LocalDateTime.parse(input, formatter);
            return LocalDateTime.from(localDateTime);
        } catch (DateTimeException ex) {
            System.out.println("Не удалось прочитать localDateTime");
            return null;
        }
    }
}
