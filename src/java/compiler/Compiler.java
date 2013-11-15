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
import java.util.Random;
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
    private static final String CODE_TEMPLATE_PATH = "C:\\Users\\Administrator\\Documents\\NetBeansProjects\\OnlineCompiler\\web\\code\\";
    private static final String SOURCE_FILE_BASE_PATH = "C:\\cpc\\";
    private String sourceFileName = "";
    private String outputFileName = "";
    private void precompile(String qno,String language,String snippet){        
        Random generator = new Random(System.currentTimeMillis());
        sourceFileName = generator.nextInt(200000)+"";
        outputFileName = sourceFileName;
        sourceFileName += ".c";
        sourceFile = new File(SOURCE_FILE_BASE_PATH+sourceFileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(sourceFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            if(sourceFile.exists()){
                sourceFile.delete();
            }
            sourceFile.createNewFile();
            sourceFile.deleteOnExit();
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        File header = new File(CODE_TEMPLATE_PATH+language+"\\head"+qno);
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
        File tail = new File(CODE_TEMPLATE_PATH+language+"\\tail"+qno);
        if(tail.exists()){
            try {
                FileInputStream fis = new FileInputStream(tail);                
                int x = 0;                
                while((x=fis.read())!=-1){
                    fos.write(x);
                }
                fis.close();
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        //System.out.println("*****************************************");
        //System.out.println(tail.exists()+" "+tail.getAbsolutePath());        
        //System.out.println("*****************************************");
        //message = "precompile over";       
    }
    
    private boolean compile(){
        Runtime rt = Runtime.getRuntime();        
        String cmd = "gcc "+SOURCE_FILE_BASE_PATH+sourceFile.getName()+" -o "+SOURCE_FILE_BASE_PATH+outputFileName;
        try {
            Process p = rt.exec(cmd);
            p.waitFor();
            if(p.exitValue()!=0){
                Scanner sc = new Scanner(p.getErrorStream());
                while(sc.hasNext()){
                    message += sc.nextLine();
                }
                sourceFile.delete();
                return false;
            }
            message = "Compile success";
            sourceFile.delete();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(sourceFile.exists()){
            sourceFile.delete();
        }
        return false;
    }
    
    private boolean runProgram(){
        Runtime rt = Runtime.getRuntime();
        String cmd = SOURCE_FILE_BASE_PATH+outputFileName+".exe";
        File opFile = new File(cmd);
        try {
            Process p = rt.exec(cmd);
            p.getInputStream();
            Thread.sleep(2000);
            try{
                int r = p.exitValue();
                if(r==0){
                    message = "Execution successful";
                    if(opFile.exists()){
                        opFile.delete();
                    }
                    return true;                    
                }                
            }catch(IllegalThreadStateException e){                
                p.destroy();
                if(opFile.exists()){
                    opFile.delete();
                }
                message = "Time Limit exceeded";
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        message = "Test case failed";
        if(opFile.exists()){
             opFile.delete();
        }
        return false;
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
            if(compile()){
                runProgram();
            }
            out.println(message);
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
