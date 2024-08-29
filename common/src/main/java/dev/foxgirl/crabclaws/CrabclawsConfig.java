package dev.foxgirl.crabclaws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import dev.architectury.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CrabclawsConfig {

    public double clawExtraReachAmount = 3.0;

    public boolean shouldSpawnClawsInRuins = true;
    public int probabilityOfClawsInRuins = 7;

    private static final CrabclawsConfig CONFIG = loadConfig();

    private static CrabclawsConfig loadConfig() {
        Logger logger = LogManager.getLogger();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        Path configPath = Platform.getConfigFolder().resolve("crabclaws.json");
        Path tempPath = Platform.getConfigFolder().resolve("crabclaws.json.tmp");

        try {
            return gson.fromJson(Files.newBufferedReader(configPath), CrabclawsConfig.class);
        } catch (NoSuchFileException cause) {
            logger.warn("Config file not found, will be created");
        } catch (JsonParseException cause) {
            logger.error("Failed to parse config file", cause);
        } catch (Exception cause) {
            logger.error("Failed to load config file", cause);
        }

        CrabclawsConfig config = new CrabclawsConfig();

        try {
            Files.writeString(tempPath, gson.toJson(config, CrabclawsConfig.class));
            Files.move(tempPath, configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception cause) {
            logger.error("Failed to save new config file", cause);
        }

        return config;
    }

    public static CrabclawsConfig getConfig() {
        return CONFIG;
    }

}
