package cn.jiongjionger.neverlag.fixer;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import cn.jiongjionger.neverlag.config.ConfigManager;

public class AntiUseEggsChangeSpawnerType implements Listener {

	private final ConfigManager cm = ConfigManager.getInstance();

	// 将优先级设为NORMAL以与各种小游戏插件兼容. LOWEST可能破坏一些小游戏的游戏机制
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!cm.isAntiUseEggsChangeSpawnerType()) {
			return;
		}
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock().getType().equals(Material.MOB_SPAWNER)) {
				if (e.getItem() == null) {
					return;
				}
				if (e.getPlayer().isOp()) {
					return;
				}
				if (e.getItem().getType().equals(Material.MONSTER_EGG) || e.getItem().getType().equals(Material.MONSTER_EGGS)) {
					e.setCancelled(true);
				}
			}
		}
	}
}
