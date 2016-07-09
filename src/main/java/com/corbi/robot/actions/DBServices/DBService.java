/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corbi.robot.actions.DBServices;

import com.corbi.robot.objects.Game;
import com.corbi.robot.objects.User;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that handles anny dbcalls
 *
 * @author PogChamp
 */
public class DBService {

    Connection con;
    private static final String TABLENAME = "HydraBotDB";

    public DBService(String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:postgresql:" + TABLENAME,
                    username,
                    password);
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DBService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
public UserService getUserService(){
     return new UserService(TABLENAME, con);
 }

public GameService getGameService(){
    return new GameService(TABLENAME, con);
}
public SoundService getSoundService(){
    return new SoundService(TABLENAME, con);
}
    /**
     * This method sends a SQL-Statement to the database
     *
     * @param statement SQL-Statement that should be sent to the database
     * @throws SQLException
     */
    protected static void execute(PreparedStatement statement) throws SQLException {
        statement.execute();
        statement.close();
    }
}
