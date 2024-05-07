package cn.lelmc.itemmanage;

import cn.lelmc.itemmanage.config.Config;
import cn.lelmc.itemmanage.config.ConfigLoader;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class util {
    //编辑物品为绑定
    public static ItemStack EditBind(ItemStack itemStack, Player player) {
        List<Text> lore = getLore(itemStack);
        lore.add(Text.of(util.regex(ConfigLoader.instance.getConfig().general.BindLore
                .replace("%player%", player.getName()))));

        itemStack.offer(Keys.ITEM_LORE, lore);
        DataContainer dataContainer = itemStack.toContainer();
        dataContainer.set(DataQuery.of("UnsafeData", "bind"), player.getUniqueId().toString());
        return ItemStack.builder().fromContainer(dataContainer).build();
    }

    //编辑物品保质期
    public static ItemStack EditTime(ItemStack itemStack, int time) {
        Config.General general = ConfigLoader.instance.getConfig().general;
        long l = System.currentTimeMillis();
        long t2 = (long) time * 60 * 1000;

        List<Text> lore = getLore(itemStack);
        lore.add(0, Text.of(util.regex(general.TimeLimitLore
                .replace("%time%", new SimpleDateFormat(general.TimeLimitForm).format(l + t2)))));
        itemStack.offer(Keys.ITEM_LORE, lore);
        DataContainer dataContainer = itemStack.toContainer();
        dataContainer.set(DataQuery.of("UnsafeData", "expire"), l + t2);

        return ItemStack.builder().fromContainer(dataContainer).build();
    }

    //检查是否过期
    public static Boolean isExpired(DataContainer data) {
        if (data.get(DataQuery.of("UnsafeData", "expire")).isPresent()) {
            Long o = (Long) data.get(DataQuery.of("UnsafeData", "expire")).get();
            return System.currentTimeMillis() >= o;
        }
        return false;
    }

    //检查绑定信息
    public static Boolean isBind(DataContainer data, Player player) {
        if (data.get(DataQuery.of("UnsafeData", "bind")).isPresent()) {
            String uuid = data.get(DataQuery.of("UnsafeData", "bind")).get().toString();
            if (!uuid.equals(player.getUniqueId().toString())) {
                player.sendMessage(Text.of(regex(ConfigLoader.instance.getConfig().messages.bClick)));
                return true;
            }
        }
        return false;
    }

    //kit绑定物品
    public static ItemStackSnapshot kitEdit(ItemStackSnapshot itemStackSnapshot, Player player) {
        Config.General general = ConfigLoader.instance.getConfig().general;
        if (itemStackSnapshot.createStack().get(Keys.ITEM_LORE).isPresent()) {
            List<Text> texts = itemStackSnapshot.createStack().get(Keys.ITEM_LORE).get();
            ItemStack stack = itemStackSnapshot.createStack();
            if (texts.toString().contains(general.TimeKitLore)){
                Integer time = getInt(texts.get(0).toString());
                long l = System.currentTimeMillis();
                long t2 = (long) time * 60 * 1000;
                texts.set(0, Text.of(util.regex(general.TimeLimitLore
                        .replace("%time%", new SimpleDateFormat(general.TimeLimitForm).format(l + t2)))));
                stack.offer(Keys.ITEM_LORE, texts);
                DataContainer set = stack.toContainer().set(DataQuery.of("UnsafeData", "expire"), l + t2);
                stack = ItemStack.builder().fromContainer(set).build();
            }
            if (texts.toString().contains(general.BindKitLore)) {
                texts.remove(Text.of(general.BindKitLore));
                texts.add(Text.of(util.regex(ConfigLoader.instance.getConfig().general.BindLore
                        .replace("%player%", player.getName()))));

                stack.offer(Keys.ITEM_LORE, texts);
                DataContainer set = stack.toContainer().set(DataQuery.of("UnsafeData", "bind"), player.getUniqueId().toString());
                stack = ItemStack.builder().fromContainer(set).build();
            }
            return stack.createSnapshot();
        }
        return itemStackSnapshot;
    }

    //获取Lore
    public static List<Text> getLore(ItemStack itemStack) {
        if (itemStack.get(Keys.ITEM_LORE).isPresent()) {
            return itemStack.get(Keys.ITEM_LORE).get();
        } else {
            return new ArrayList<>();
        }
    }

    //正则检查是不是int
    public static Boolean isInt(String s) {
        String regex = "\\d+";
        return Pattern.compile(regex).matcher(s).matches();
    }

    //获取字符串中的int
    public static Integer getInt(String s){
        String regex = "[^(\\d)]";
        String ticket = Pattern.compile(regex).matcher(s)
                .replaceAll("").trim();
        return Integer.parseInt(ticket);
    }

    public static String regex(String msg) {
        return msg.replace("&", "§");
    }

}
