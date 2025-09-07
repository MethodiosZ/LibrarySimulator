/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditBooksTable;
import database.tables.EditLibrarianTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Librarians;

/**
 *
 * @author MethodiosZach
 */
public class Librarian extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param req servlet request
     * @param res servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void handleRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
            
        BufferedReader reader = req.getReader();
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String rawdata = buffer.toString();
        String data = rawdata.substring(0, rawdata.length() - 1);
        String id = rawdata.substring(rawdata.length() - 1, rawdata.length());
        String paramvalue = req.getHeader("reqtype");
        if (paramvalue.equals("addbookstock")) {
            EditBookStock(data, id, "true");
            res.setStatus(200);
        } else if (paramvalue.equals("removebookstock")) {
            EditBookStock(data, id, "false");
            res.setStatus(200);
        } else if (paramvalue.equals("addbook")) {
            EditBooksTable book = new EditBooksTable();
            EditBooksInLibraryTable libbook = new EditBooksInLibraryTable();
            book.addBookFromJSON(data);
            libbook.addBookInLibraryFromJSON(data);
            res.setStatus(200);
        } else if (paramvalue.equals("changedetails")) {
            EditLibrarianTable lib = new EditLibrarianTable();
            Librarians update = lib.jsonToLibrarian(data);
            lib.updateLibrarian(update);
            res.setStatus(200);
        } else {
            res.setStatus(400);
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
        try {
            handleRequest(request, response);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Librarian.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            handleRequest(request, response);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Librarian.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void EditBookStock(String isbn, String id, String edit) throws SQLException, ClassNotFoundException {
        EditBooksInLibraryTable book = new EditBooksInLibraryTable();
        book.updateBookInLibrary(isbn, id, edit);
    }
}
