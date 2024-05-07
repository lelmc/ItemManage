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
public class TimeLimit implements CommandCallable {
    @Override
    public CommandResult process(CommandSource source, String arguments) {
        String[] args = arguments.split(" ");
        Config.Messages msg = ConfigLoader.instance.getConfig().messages;
        if (args.length == 2 && args[0].equals("add") && source instanceof Player) {
            Player player = (Player) source;
            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                if (player.getItemInHand(HandTypes.MAIN_HAND).get().equals(ItemStack.of(ItemTypes.AIR))){
                    source.sendMessage(Text.of(util.regex(msg.tEditNoItem)));
                    return CommandResult.success();
                }
            }
            if (!util.isInt(args[1])) {
                source.sendMessage(Text.of(util.regex(msg.tEditNoTime)));
                return CommandResult.success();
            }

            ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
            int time = Integer.parseInt(args[1]);//物品保质期
            ItemStack item = util.EditTime(itemStack, time);
            player.setItemInHand(HandTypes.MAIN_HAND, item);
            source.sendMessage(Text.of(util.regex(msg.tEditSuccess)));
        } else if (args.length == 5 && args[0].equals("give")) {
            if (!Sponge.getServer().getPlayer(args[1]).isPresent()) {
                source.sendMessage(Text.of(util.regex(msg.tGiveNoPlayer)));
                return CommandResult.success();
            }

            if (!Sponge.getGame().getRegistry().getType(ItemType.class, args[2]).isPresent()) {
                source.sendMessage(Text.of(util.regex(msg.tGiveNoItem)));
                return CommandResult.success();
            }

            if (!util.isInt(args[3])) {
                source.sendMessage(Text.of(util.regex(msg.tGiveNoItemSum)));
                return CommandResult.success();
            } else if (!util.isInt(args[4])) {
                source.sendMessage(Text.of(util.regex(msg.tGiveNoTime)));
                return CommandResult.success();
            }

            Player player = Sponge.getServer().getPlayer(args[1]).get();
            ItemType itemType = Sponge.getGame().getRegistry().getType(ItemType.class, args[2]).get();//物品
            int i = Integer.parseInt(args[3]);//物品数量
            int time = Integer.parseInt(args[4]);//物品保质期
            ItemStack itemStack = ItemStack.of(itemType, i);
            ItemStack item = util.EditTime(itemStack, time);
            player.getInventory().offer(item);
            source.sendMessage(Text.of(util.regex(msg.tGiveSuccess.replace("%player%", player.getName()))));
        } else {
            source.sendMessage(Text.of(getUsage(source)));
            return CommandResult.success();
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
        return Text.of(util.regex(ConfigLoader.instance.getConfig().messages.tHelp));
    }

}
