package dev.snowsylph.zerotick.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.snowsylph.zerotick.Logger;
import dev.snowsylph.zerotick.ZeroTick;

public class Config {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File path = new File("./config/" + ZeroTick.MOD_ID + ".json");
    private static Bean cfg = new Bean();

    public static Bean config() {
        return cfg;
    }

    public static void write() {
        if (!path.getParentFile().exists() && !path.getParentFile().mkdirs())
        {
            Logger.err("Failed to write config to disc.");
            return;
        }

        try (FileWriter file = new FileWriter(path))
        {
            file.write(gson.toJson(cfg));
            file.flush();
        }
        catch (IOException e)
        {
            Logger.err("Failed to write config to disc.");
        }
    }

    public static void read() {
        try (FileReader file = new FileReader(path))
        {
            Config.cfg = new Gson().fromJson(file, Bean.class);
        }
        catch (IOException e)
        {
            Logger.err("Failed to read config from file.");
        }
    }

    private Config() {}
}
