package cn.lelmc.itemmanage.commands;

import cn.lelmc.itemmanage.config.Config;
import cn.lelmc.itemmanage.config.ConfigLoader;
import cn.lelmc.itemmanage.util;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@NonnullByDefault
public class Bind implements CommandCallable {

    @Override
    public CommandResult process(CommandSource source, String arguments) {
        String[] args = arguments.split(" ");
        Config.Messages msg = ConfigLoader.instance.getConfig().messages;
        if (args.length == 2 && args[0].equals("add") && source instanceof Player) {
            Player player = (Player) source;
            if (!Sponge.getServer().getPlayer(args[1]).isPresent()) {
                source.sendMessage(Text.of(util.regex(msg.bEditNoPlayer)));
                return CommandResult.success();
            }

            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                if (player.getItemInHand(HandTypes.MAIN_HAND).get().equals(ItemStack.of(ItemTypes.AIR))){
                    source.sendMessage(Text.of(util.regex(msg.bEditNoItem)));
                    return CommandResult.success();
                }
            }
            Player toPlayer = Sponge.getServer().getPlayer(args[1]).get();
            ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
            ItemStack item = util.EditBind(itemStack, toPlayer);
            player.setItemInHand(HandTypes.MAIN_HAND, item);
            source.sendMessage(Text.of(util.regex(msg.bEditSuccess.replace("%player%", toPlayer.getName()))));
        } else if (args.length == 4 && args[0].equals("give")){
            if (!Sponge.getServer().getPlayer(args[1]).isPresent()) {
                source.sendMessage(Text.of(util.regex(msg.bGiveNoPlayer)));
                return CommandResult.success();
            }
            if (!Sponge.getGame().getRegistry().getType(ItemType.class, args[2]).isPresent()) {
                source.sendMessage(Text.of(util.regex(msg.bGiveNoItem)));
                return CommandResult.success();
            }
            if (!util.isInt(args[3])) {
                source.sendMessage(Text.of(util.regex(msg.bGiveNoItemSum)));
                return CommandResult.success();
            }
            Player player = Sponge.getServer().getPlayer(args[1]).get();
            ItemType itemType = Sponge.getGame().getRegistry().getType(ItemType.class, args[2]).get();//物品
            int i = Integer.parseInt(args[3]);//物品数量
            ItemStack item = util.EditBind(ItemStack.of(itemType, i), player);
            player.getInventory().offer(item);
            source.sendMessage(Text.of(util.regex(msg.bGiveSuccess.replace("%player%", player.getName()))));
        }else {
            source.sendMessage(Text.of(getUsage(source)));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) {
        if (arguments.equals("")){
            return Arrays.asList("add","give");
        } else {
            return Sponge.getGame().getServer().getOnlinePlayers().stream().map(User::getName).collect(Collectors.toList());
        }
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return source.hasPermission("ItemManage.admin");//权限
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return Optional.empty();
    }

    @Override
    public Text getUsage(CommandSource source) {
        return Text.of(ConfigLoader.instance.getConfig().messages.bHelp);
    }
}
