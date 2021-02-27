package com.oldschoolminecraft.nrp.sql;

import com.oldschoolminecraft.nrp.Plugin;
import org.bukkit.util.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SQLThread extends Thread
{
    private SQLEventPipe eventPipe;
    private PreparedStatement preparedStatement;

    public SQLThread(SQLEventPipe eventPipe)
    {
        this.eventPipe = eventPipe;
    }

    public void prepareStatement(String statement, String types, Object... values)
    {
        try
        {
            Configuration config = Plugin.instance.getConfiguration();
            String sql_host = config.getString("sql_host", "localhost");
            String sql_username = config.getString("sql_username", "root");
            String sql_password = config.getString("sql_password", "");

            Connection con = DriverManager.getConnection(String.format("jdbc:mysql://%s/nrp?user=%s&password=%s", sql_host, sql_username, sql_password));

            PreparedStatement stmt = con.prepareStatement(statement);

            if (types.length() != values.length)
                throw new RuntimeException("Types and values do not match (do you have more values than types?)");

            for (int i = 0; i < values.length; i++)
            {
                char type = types.toLowerCase().charAt(i);
                switch (type)
                {
                    case 's':
                        stmt.setString(i + 1, (String) values[i]);
                        break;
                    case 'i':
                        stmt.setInt(i + 1, (int) values[i]);
                        break;
                    case 'b':
                        stmt.setBoolean(i + 1, (boolean) values[i]);
                        break;
                }
            }

            this.preparedStatement = stmt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            if (this.preparedStatement.execute())
                eventPipe.fire(true, this.preparedStatement);
            else
                eventPipe.fire(false, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            eventPipe.fire(false, null);
        }
    }
}
