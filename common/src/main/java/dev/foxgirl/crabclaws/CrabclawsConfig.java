package dev.foxgirl.crabclaws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import dev.architectury.platform.Platform;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CrabclawsConfig {

    public double clawExtraReachAmount = 3.0;

    public boolean shouldSpawnClawsInRuins = true;
    public int probabilityOfClawsInRuins = 7;

    public static CrabclawsConfig loadConfig() {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        Path configPath = Platform.getConfigFolder().resolve("crabclaws.json");
        Path tempPath = Platform.getConfigFolder().resolve("crabclaws.json.tmp");

        try {
            return gson.fromJson(Files.newBufferedReader(configPath), CrabclawsConfig.class);
        } catch (NoSuchFileException cause) {
            CrabclawsImpl.LOGGER.warn("Config file not found, will be created");
        } catch (JsonParseException cause) {
            CrabclawsImpl.LOGGER.error("Failed to parse config file", cause);
        } catch (Exception cause) {
            CrabclawsImpl.LOGGER.error("Failed to load config file", cause);
        }

        CrabclawsConfig config = new CrabclawsConfig();

        try {
            Files.writeString(tempPath, gson.toJson(config, CrabclawsConfig.class));
            Files.move(tempPath, configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception cause) {
            CrabclawsImpl.LOGGER.error("Failed to save new config file", cause);
        }

        return config;
    }

}
