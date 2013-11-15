/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Compiler", urlPatterns = {"/Compiler"})
public class Compiler extends HttpServlet {
    File sourceFile;
    String message;
    private void precompile(String qno,String language,String snippet){
        // TODO: Create a temporary file and then add the contents.
        sourceFile = new File("/cpc/ghj.c");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(sourceFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        sourceFile.deleteOnExit();
        try {
            if(sourceFile.exists()){
                sourceFile.delete();
            }
            sourceFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        File header = new File("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\OnlineCompiler\\web\\code\\"+language+"\\head"+qno);
        if(header.exists()){
            try {
                FileInputStream fis = new FileInputStream(header);
                
                int x = 0;                
                while((x=fis.read())!=-1){
                    fos.write(x);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        try {
            // Insert code snippet
            fos.write(snippet.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Insert tail
        File tail = new File("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\OnlineCompiler\\web\\code\\"+language+"\\tail"+qno);
        if(tail.exists()){
            try {
                FileInputStream fis = new FileInputStream(tail);                
                int x = 0;                
                while((x=fis.read())!=-1){
                    fos.write(x);;
                }
                fis.close();
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
         System.out.println("*****************************************");
        System.out.println(tail.exists()+" "+tail.getAbsolutePath());        
        System.out.println("*****************************************");
        message = "precompile over";       
    }
    
    private boolean compile(){
        Runtime rt = Runtime.getRuntime();
        String cmd = "gcc c:\\cpc\\"+sourceFile.getName();
        try {
            Process p = rt.exec(cmd);
            p.waitFor();
            if(p.exitValue()!=0){
                Scanner sc = new Scanner(p.getErrorStream());
                while(sc.hasNext()){
                    message += sc.nextLine();
                }
                return false;
            }
            message = "Compile success";
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void run(){
        // TODO: Get instructions on how to run
    }
    
    private void getOutput(){
        // TODO: Finally check the status of program and return
    }

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
        String code = request.getParameter("code");
        String language = request.getParameter("language");
        String qno = request.getParameter("qno");
        precompile(qno, language, code);
        
        try {
            if(!compile()){
                out.println("failure");
            }else{
                out.println("compiled");
            }
            out.println("Compile message"+message);
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
