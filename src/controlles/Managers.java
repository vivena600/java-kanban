package controlles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.adapters.DurationAdapters;
import server.adapters.LocalDateTimeAdapters;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getJson() {
        return new GsonBuilder()
                //.serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapters())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapters())
                .create();
    }
}
