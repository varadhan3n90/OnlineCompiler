/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class SaveUserCode extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int qid = Integer.parseInt(request.getParameter("qid"));
            String username = (String) request.getSession().getAttribute("user");
            String code = request.getParameter("code");
            int load = Integer.parseInt(request.getParameter("load"));
            Connection conn = (Connection) getServletContext().getAttribute("dbconn");
            String maxTID = "select max(testid) as testid from moodle.mdl_prg_ques;";
            PreparedStatement ps = conn.prepareStatement(maxTID);
            ResultSet rs = ps.executeQuery();
            rs.next();            
            int tid = rs.getInt(1);
            if(load==0){
                //System.out.println("qid "+qid+" username "+username);                                
                String sql = "insert into moodle.mdl_prg_save(tid,qid,username,code) values(?,?,?,?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,tid);
                ps.setInt(2, qid);
                ps.setString(3, username);
                ps.setString(4, code);
                ps.executeUpdate();
                out.println("Code saved");
            }else{
                String sql = "select code from moodle.mdl_prg_save where tid=? and qid=? and username=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,tid);
                ps.setInt(2, qid);
                ps.setString(3, username);
                rs = ps.executeQuery();
                if(rs.next()){
                    out.println(rs.getString(1));
                }else{
                    out.println("No code previously saved");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaveUserCode.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
