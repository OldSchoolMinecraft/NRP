package com.oldschoolminecraft.nrp.handlers;

import com.oldschoolminecraft.nrp.Plugin;
import com.oldschoolminecraft.nrp.sql.SQLThread;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.commands.Command;
import ru.tehkode.permissions.commands.CommandListener;

import java.util.Map;

public class CommandHandler implements CommandListener
{
    @Command(name = "recruit", syntax = "", description = "Display your recruitment code", permission = "nrp.use")
    public void onRecruitCommand(final Plugin plugin, final CommandSender sender, final Map<String, String> args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only players are allowed to use this command!");
            return;
        }

        Player ply = (Player) sender;

        SQLThread thread = new SQLThread((success, stmt) ->
        {
            try
            {
                int count = 0;
                while (stmt.getResultSet().next())
                    count++;
                if (count == 0)
                {
                    String newCode = Plugin.instance.generateRandomString(8);
                    ply.sendMessage("A new recruitment code has been generated for you.");
                    ply.sendMessage("Your recruitment code is: " + newCode);

                    SQLThread sThread = new SQLThread((s1, s2) ->
                    {
                        if (!s1)
                            ply.sendMessage("Failed to submit new code to database. If this error persists, please contact a member of staff.");
                    });

                    sThread.prepareStatement("INSERT INTO recruiting (username, code) VALUES (?, ?)", "ss", ply.getName(), newCode);
                    sThread.start();
                } else {
                    if (success)
                        ply.sendMessage("Your recruitment code is: " + stmt.getResultSet().getString("code"));
                    else
                        ply.sendMessage(ChatColor.RED + "SQL query failed unexpectedly. If this error persists, please contact a member of staff.");
                }
            } catch (Exception ex) {
                ply.sendMessage(ChatColor.RED + "An unexpected error has occurred. If this error persists, please contact a member of staff.");
            }
        });

        thread.prepareStatement("SELECT * FROM recruiting WHERE username = ?", "s", ply.getName());
        thread.start();
    }

    @Command(name = "recruitment", syntax = "<code>", description = "Redeem a recruitment code", permission = "nrp.use")
    public void onRecruitmentCommand(final Plugin plugin, final CommandSender sender, final Map<String, String> args)
    {
        //TODO: if player meets the requirements, give rewards to recruiter.
        //TODO: if the player does NOT meet the requirements, submit recruitment information to database
    }
}
