package com.oldschoolminecraft.nrp.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface SQLEventPipe
{
    void fire(boolean success, PreparedStatement stmt);
}
