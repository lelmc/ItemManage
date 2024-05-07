ItemManage
===========================
sponge版的简单物品管理插件

# 目前实现功能：

    物品限时 
    物品绑定
# 支持 nucleus礼包lore描述自动添加功能
    lore语法：
    绑定           - 玩家领取后自动绑定给该玩家
    限时：(分钟)    - 如：限时：10  玩家领取后自动设置为限时10分钟的物品

****
| 命令                            | 功能           | 权限               |
|-------------------------------|--------------|------------------|
| /imReload                     | 重载配置文件       | ItemManage.admin |
| /itl add 时间(分钟)               | 给物品添加限时      | ItemManage.admin |
| /itl give 玩家ID 物品 物品数量 时间(分钟) | 给予玩家限时物品     | ItemManage.admin |
| /bd add 玩家ID                  | 手中的物品绑定给指定玩家 | ItemManage.admin |
| /bd give 玩家ID 物品 物品数量         | 给予玩家一个绑定物品   | ItemManage.admin |
****