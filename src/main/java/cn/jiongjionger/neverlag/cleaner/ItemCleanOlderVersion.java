package cn.jiongjionger.neverlag.cleaner;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import cn.jiongjionger.neverlag.NeverLag;
import cn.jiongjionger.neverlag.config.ConfigManager;
import cn.jiongjionger.neverlag.utils.EntityUtils;

public class ItemCleanOlderVersion {

	private static ConfigManager cm = ConfigManager.getInstance();

	@SuppressWarnings("deprecation")
	public static void doClean() {
		if (!cm.isClearDropItem()) {
			return;
		}
		int count = 0;
		for (World world : Bukkit.getWorlds()) {
			// 如果当前世界不在排除列表
			if (!cm.getNoClearItemWorld().contains(world.getName())) {
				for (Entity entity : world.getEntities()) {
					if (entity == null) {
						continue;
					}
					if (entity instanceof Item && cm.isClearItem()) {
						Item item = (Item) entity;
						// 判断是否在不清理的物品ID白名单
						if (!cm.getNoClearItemId().contains(item.getItemStack().getTypeId())) {
							// 玩家附近的时候是否还清理
							if (!cm.isClearItemPlayerNearby() && hasPlayerNearby(item, cm.getClearItemPlayerNearbyDistance())) {
								continue;
							}
							entity.remove();
							count++;
						}
					} else if (entity instanceof ItemFrame && cm.isClearItemFrame()) {
						entity.remove();
						count++;
					} else if (entity instanceof Boat && cm.isClearBoat()) {
						entity.remove();
						count++;
					} else if (entity instanceof ExperienceOrb && cm.isClearExpBall()) {
						entity.remove();
						count++;
					} else if (entity instanceof FallingBlock && cm.isClearFallingBlock()) {
						entity.remove();
						count++;
					} else if (entity instanceof Painting && cm.isClearPainting()) {
						entity.remove();
						count++;
					} else if (entity instanceof Minecart && cm.isClearMinecart()) {
						entity.remove();
						count++;
					} else if (entity instanceof Arrow && cm.isClearArrow()) {
						entity.remove();
						count++;
					} else if (entity instanceof Snowball && cm.isClearSnowBall()) {
						entity.remove();
						count++;
					}
				}
			}
		}
		// 公告
		if (cm.isBroadcastClearItem()) {
			Bukkit.getServer().broadcastMessage(cm.getClearItemBroadcastMessage().replace("%COUNT%", String.valueOf(count)));
		}
	}

	/*
	 * 判断掉落物附近有没有玩家
	 * 
	 * @param item 掉落物
	 * 
	 * @param distance 判断距离
	 * 
	 * @return 是否存在玩家
	 */
	private static boolean hasPlayerNearby(Item item, int distance) {
		for (Entity entity : item.getNearbyEntities(distance, distance, distance)) {
			if (entity instanceof Player) {
				if (!EntityUtils.checkCustomNpc(entity)) {
					return true;
				}
			}
		}
		return false;
	}

	private int preMessageTime = 0;

	public ItemCleanOlderVersion() {
		NeverLag.getInstance().getServer().getScheduler().runTaskTimer(NeverLag.getInstance(), new Runnable() {
			@Override
			public void run() {
				doClean();
			}
		}, cm.getClearItemDelay() * 20L, cm.getClearItemDelay() * 20L);
		if (cm.getClearItemDelay() > 60) {
			NeverLag.getInstance().getServer().getScheduler().runTaskTimer(NeverLag.getInstance(), new Runnable() {
				@Override
				public void run() {
					doPreMessage();
				}
			}, 20L, 20L);
		}
	}

	// 提前通知
	private void doPreMessage() {
		if (cm.isClearDropItem() && cm.isBroadcastClearItem()) {
			this.preMessageTime++;
			int remainTick = cm.getClearItemDelay() - this.preMessageTime;
			switch (remainTick) {
			case 60:
				Bukkit.getServer().broadcastMessage(cm.getClearItemBroadcastPreMessage().replace("%TIME%", "60"));
				break;
			case 30:
				Bukkit.getServer().broadcastMessage(cm.getClearItemBroadcastPreMessage().replace("%TIME%", "30"));
				break;
			case 10:
				Bukkit.getServer().broadcastMessage(cm.getClearItemBroadcastPreMessage().replace("%TIME%", "10"));
				break;
			default:
				break;
			}
			if (remainTick <= 0) {
				this.preMessageTime = 0;
			}
		}
	}
}
