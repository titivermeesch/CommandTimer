package me.playbosswar.com;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListeners implements Listener {

    @EventHandler
    public void listCommandsGUIEvents(InventoryClickEvent e) { //List commands GUI
        Player p = (Player) e.getWhoClicked();
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        if(e.getView().getTitle().equalsIgnoreCase("All loaded timers")) {
            e.setCancelled(true);
            if(e.getSlot() == 53) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            int id = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName());
            e.setCancelled(true);
            Main.getPlugin().getConfig().set("tasks." + id , null);
            Main.getPlugin().saveConfig();
            GUIHandler.listCommandsGUI(p);
        }
    }
}
