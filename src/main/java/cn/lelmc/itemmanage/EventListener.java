package cn.lelmc.itemmanage;

import cn.lelmc.itemmanage.config.ConfigLoader;
import io.github.nucleuspowered.nucleus.api.events.NucleusKitEvent;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.*;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class EventListener {

    @Listener
    public void onKit(NucleusKitEvent.Redeem.Pre event){
        Player player = event.getTargetEntity();
        Collection<ItemStackSnapshot> kit = new ArrayList<>();
        for (ItemStackSnapshot itemStackSnapshot : event.getOriginalStacksToRedeem()) {
            kit.add(util.kitEdit(itemStackSnapshot, player));
        }
        event.setStacksToRedeem(kit);
    }

    @Listener//捡取物品事件
    public void ChangeInventory(ChangeInventoryEvent.Pickup.Pre event, @First Player player) {
        DataContainer data = event.getOriginalStack().toContainer();
        if (util.isBind(data, player) && !player.hasPermission("ItemManage.admin")) {
            event.setCancelled(true);
        }
    }

    @Listener//点击物品时触发
    public void ClickInventory(ClickInventoryEvent.Primary event, @First Player player) {
        for (SlotTransaction transaction : event.getTransactions()) {
            DataContainer data = transaction.getOriginal().toContainer();
            if (util.isExpired(data)) {
                event.getCursorTransaction().setCustom(ItemStackSnapshot.NONE);
                player.sendMessage(Text.of(util.regex(ConfigLoader.instance.getConfig().messages.tClick)));
            }
            //检查物品是否绑定
            if (util.isBind(data, player) && !player.hasPermission("ItemManage.admin")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener//右键物品时触发
    public void ClickInventory(ClickInventoryEvent.Secondary event, @First Player player) {
        for (SlotTransaction transaction : event.getTransactions()) {
            DataContainer data = transaction.getOriginal().toContainer();
            if (util.isExpired(data)) {
                event.getCursorTransaction().setCustom(ItemStackSnapshot.NONE);
                player.sendMessage(Text.of(util.regex(ConfigLoader.instance.getConfig().messages.tClick)));
            }
            //检查物品是否绑定
            if (util.isBind(data, player) && !player.hasPermission("ItemManage.admin")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener//Shift物品时触发
    public void ClickInventory(ClickInventoryEvent.Shift event, @First Player player) {
        for (SlotTransaction transaction : event.getTransactions()) {
            DataContainer data = transaction.getOriginal().toContainer();
            if (util.isExpired(data)) {
                event.setCancelled(true);
                player.sendMessage(Text.of(util.regex(ConfigLoader.instance.getConfig().messages.tClick)));
            }
            //检查物品是否绑定
            if (util.isBind(data, player) && !player.hasPermission("ItemManage.admin")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener//数字交换物品时触发
    public void ClickInventory(ClickInventoryEvent.NumberPress event, @First Player player) {
        for (SlotTransaction transaction : event.getTransactions()) {
            DataContainer data = transaction.getOriginal().toContainer();
            if (util.isExpired(data)) {
                event.setCancelled(true);
                player.sendMessage(Text.of(util.regex(ConfigLoader.instance.getConfig().messages.tClick)));
            }
            //检查物品是否绑定
            if (util.isBind(data, player) && !player.hasPermission("ItemManage.admin")) {
                event.setCancelled(true);
            }
        }
    }

    @Listener//清理过期物品
    public void Join(ClientConnectionEvent.Join event, @First Player player) {
        Inventory inventory = player.getInventory().query(PlayerInventory.class);
        for (int i = 0; i < 36; i++) {
            Optional<Slot> slot = ((PlayerInventory) inventory).getMain().getSlot(new SlotIndex(i));
            if (slot.isPresent() && slot.get().peek().isPresent()) {
                DataContainer data = slot.get().peek().get().toContainer();
                if (util.isExpired(data)) {
                    slot.get().clear();
                }
            }
        }
    }

}
