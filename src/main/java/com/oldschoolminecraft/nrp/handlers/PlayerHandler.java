package com.oldschoolminecraft.nrp.handlers;

import com.oldschoolminecraft.nrp.sql.SQLThread;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerHandler extends PlayerListener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        //TODO: check if player has any pending recruitments that have become eligible for rewards

        SQLThread thread = new SQLThread(((success, stmt) ->
        {
            //
        }));

        thread.prepareStatement("SELECT * FROM pending WHERE recruiter_name = ?", "s", event.getPlayer().getName());
        thread.start();
    }
}
