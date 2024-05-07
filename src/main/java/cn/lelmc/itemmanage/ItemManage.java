package cn.lelmc.itemmanage;

import cn.lelmc.itemmanage.commands.Bind;
import cn.lelmc.itemmanage.commands.TimeLimit;
import cn.lelmc.itemmanage.config.ConfigLoader;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "itemmanage",
        name = "ItemManage",
        description = "ItemManage",
        authors = {
                "lelmc"
        }
)
public class ItemManage {
    public static ItemManage instance;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path path;

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        instance = this;
        CommandsRegister();
        new ConfigLoader(path);
        Sponge.getEventManager().registerListeners(this, new EventListener());
    }

    CommandSpec spec = CommandSpec.builder()
            .permission("ItemManage.admin")
            .executor((src, args) -> {
                src.sendMessage(Text.of("§6物品管理帮助信息"));
                src.sendMessage(Text.of("/imReload   §a重载配置文件"));
                src.sendMessage(Text.of("/itl   §a添加限时物品"));
                src.sendMessage(Text.of("/bd   §a绑定物品"));
                return CommandResult.success();
            })
            .build();

    CommandSpec reload = CommandSpec.builder()
            .permission("ItemManage.admin")
            .executor((src, args) -> {
                ConfigLoader.instance.load();
                src.sendMessage(Text.of("配置文件已经重新加载"));
                return CommandResult.success();
            })
            .build();

    public void CommandsRegister(){
        Sponge.getCommandManager().register(this, reload, "imReload");
        Sponge.getCommandManager().register(this, spec, "ItemManage", "im");
        Sponge.getCommandManager().register(this, new Bind(), "bind", "bd");
        Sponge.getCommandManager().register(this, new TimeLimit(), "itl");
    }

}
