package sircow.sleeplessness.config;

import com.google.gson.*;
import sircow.sleeplessness.Constants;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String SERVER_FILE_NAME = "sleeplessness.json";

    private static ServerModConfig serverConfig = new ServerModConfig();

    private ConfigManager() {}

    public static void loadServer(Path configDirectory) {
        Path configFile = configDirectory.resolve(SERVER_FILE_NAME);

        try {
            Files.createDirectories(configDirectory);
        }
        catch (IOException exception) {
            Constants.LOG.error("Failed to create the config directory", exception);
            serverConfig = new ServerModConfig();
            return;
        }

        if (!Files.exists(configFile)) {
            serverConfig = new ServerModConfig();
            saveServer(configDirectory);
            return;
        }

        boolean missingKeys;

        try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (!element.isJsonObject()) throw new IOException("Config root is not a JSON object");

            JsonObject json = element.getAsJsonObject();
            serverConfig = fromJson(json, new ServerModConfig());
            JsonObject defaults = GSON.toJsonTree(new ServerModConfig()).getAsJsonObject();
            missingKeys = !json.keySet().containsAll(defaults.keySet());
        }
        catch (Exception exception) {
            Constants.LOG.error("Failed to load {}, using defaults", configFile, exception);
            serverConfig = new ServerModConfig();
            missingKeys = true;
        }

        if (missingKeys) saveServer(configDirectory);
    }

    public static void saveServer(Path configDirectory) {
        Path configFile = configDirectory.resolve(SERVER_FILE_NAME);
        JsonObject json = new JsonObject();

        json.addProperty("wellRestedDuration", serverConfig.wellRestedDuration);
        json.addProperty("wellRestedAppliesToAllPlayers", serverConfig.wellRestedAppliesToAllPlayers);

        write(configFile, json);
    }

    public static ServerModConfig getServer() {
        return serverConfig;
    }

    private static ServerModConfig fromJson(JsonObject json, ServerModConfig config) {
        config.wellRestedDuration = getInt(json, "wellRestedDuration", config.wellRestedDuration, 1, 1000000);
        config.wellRestedAppliesToAllPlayers = getBoolean(json, "wellRestedAppliesToAllPlayers", config.wellRestedAppliesToAllPlayers);

        return config;
    }

    private static void write(Path configFile, JsonObject json) {
        Path temporaryFile = configFile.resolveSibling(configFile.getFileName() + ".tmp");

        try {
            Files.createDirectories(configFile.getParent());

            try (Writer writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                GSON.toJson(json, writer);
            }

            try {
                Files.move(temporaryFile, configFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, configFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException exception) {
            Constants.LOG.error("Failed to save {}", configFile, exception);
        }
    }

    private static int getInt(JsonObject json, String key, int defaultValue, int minimum, int maximum) {
        JsonElement element = json.get(key);

        if (element == null || !element.isJsonPrimitive()) return defaultValue;

        try {
            return Math.clamp(element.getAsInt(), minimum, maximum);
        }
        catch (RuntimeException exception) {
            return defaultValue;
        }
    }

    private static boolean getBoolean(JsonObject json, String key, boolean defaultValue) {
        JsonElement element = json.get(key);

        if (element == null || !element.isJsonPrimitive()) return defaultValue;

        try {
            return element.getAsBoolean();
        }
        catch (RuntimeException exception) {
            return defaultValue;
        }
    }
}
