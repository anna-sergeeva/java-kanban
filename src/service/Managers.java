package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.DurationTypeAdapter;
import server.LocalDateTimeAdapter;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File(".\\resources\\file1.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson () {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .setPrettyPrinting()
                .create();
    }
}

