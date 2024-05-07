package cn.lelmc.itemmanage.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {
    private Config config;
    private final Path configFile;
    public static ConfigLoader instance;
    private CommentedConfigurationNode rootNode;
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public ConfigLoader(final Path configDir) throws IOException {
        instance = this;
        // create dir directory
        configFile = configDir.resolve("setting.conf");
        if (!Files.exists(configDir)) {
            Files.createDirectory(configDir);
        }
        // create setting file
        if (!Files.exists(configFile)) {
            Files.createFile(configFile);
        }
        // try load config
        this.load();
    }

    public void load() {
        this.loader = HoconConfigurationLoader.builder().setPath(configFile).build();
        try {
            this.rootNode = this.loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true));
            this.config = this.rootNode.getValue(TypeToken.of(Config.class), new Config());
            this.save();
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        this.loader.save(this.rootNode);
    }

    public Config getConfig() {
        return config;
    }
}
