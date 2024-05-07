package cn.lelmc.itemmanage.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config {
    @Setting(comment = "全局配置")
    public General general;

    @Setting(comment = "消息配置")
    public Messages messages;

    public Config() {
        general = new General();
        messages = new Messages();
    }

    @ConfigSerializable
    public static class General {
        @Setting(comment = "Kit物品lore包含 限时：(分钟) #请确保时间在第一个位置")
        public String TimeKitLore = "限时:";
        @Setting(comment = "限时物品Lore")
        public String TimeLimitLore = "&9过期: &7%time%";
        @Setting(comment = "限时物品Lore时间格式")
        public String TimeLimitForm = "MM-dd HH:mm";

        @Setting(comment = "Kit物品包含此Lore自动绑定")
        public String BindKitLore = "绑定";
        @Setting(comment = "绑定物品Lore")
        public String BindLore = "&a绑定给: §d%player%";
    }

    @ConfigSerializable
    public static class Messages{
        @Setting(comment = "限时物品帮助信息")
        public String tHelp = "§6/itl give 玩家ID 空间物品 数量 保质期(单位：分钟) 或 §6/itl add 保质期(单位：分钟)";
        @Setting(comment = "点击过期物品提示")
        public String tClick = "这个物品已经过期";
        @Setting(comment = "编辑限时物品空手时信息")
        public String tEditNoItem = "&6请手持需要编辑的物品";
        @Setting(comment = "编辑限时物品时间错误提示信息")
        public String tEditNoTime = "&6保质期应该以分钟为单位";
        @Setting(comment = "限时物品编辑完成提示信息")
        public String tEditSuccess = "&a限时物品设置成功";

        @Setting(comment = "给予限时物品时玩家不存在提示信息")
        public String tGiveNoPlayer = "&c你输入的玩家不存在";
        @Setting(comment = "给予的限时物品不存在提示信息")
        public String tGiveNoItem = "&c输入的物品不存在";
        @Setting(comment = "给予的限时物品数量错误提示信息")
        public String tGiveNoItemSum = "&c物品数量只能为整数";
        @Setting(comment = "给予的限时物品时间错误提示信息")
        public String tGiveNoTime = "&c保质期应该以分钟为单位";
        @Setting(comment = "限时物品成功给予提示信息")
        public String tGiveSuccess = "&a成功给予玩家 &7%player% &a一个限时物品";

        @Setting(comment = "绑定物品帮助信息")
        public String bHelp = "&a/bd add 玩家ID 或 /bd give 玩家ID 空间物品 数量";
        @Setting(comment = "点击过期物品提示")
        public String bClick = "这个物品不是你的";
        @Setting(comment = "绑定物品时玩家不存在提示信息")
        public String bEditNoPlayer = "&c你输入的玩家不存在";
        @Setting(comment = "绑定物品时物品不存在提示信息")
        public String bEditNoItem = "&6请手持需要绑定的物品";
        @Setting(comment = "绑定物品成功提示信息")
        public String bEditSuccess = "&a成功将此物品绑定给了 &6%player%";

        @Setting(comment = "给予绑定物品时玩家不存在提示信息")
        public String bGiveNoPlayer = "&c你输入的玩家不存在";
        @Setting(comment = "给予绑定物品不存在提示信息")
        public String bGiveNoItem = "&c输入的物品不存在";
        @Setting(comment = "给予绑定物品数量错误提示信息")
        public String bGiveNoItemSum = "&c物品数量只能为整数";
        @Setting(comment = "给予绑定物品成功提示信息")
        public String bGiveSuccess = "&a成功将绑定物品给了 &6%player%";
    }
}
