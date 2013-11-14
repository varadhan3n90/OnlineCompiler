/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Administrator
 */
public class DBinit implements ServletContextListener{
    
    Connection conn = null;    
    String db_url = "jdbc:mysql://localhost:3306/moodle";
    String user="root",password="@min123";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(db_url,user,password);
            sce.getServletContext().setAttribute("dbconn", conn);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBinit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBinit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBinit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
