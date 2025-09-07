/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mainClasses.JSON_Converter;
import database.tables.EditBooksTable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Book;
/**
 *
 * @author MethodiosZach
 */
public class GetBooks extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSON_Converter jsonreader = new JSON_Converter();
        String json1 = jsonreader.getJSONFromAjax(request.getReader());
        JsonObject jsonObject = new JsonParser().parse(json1).getAsJsonObject();
        String genre = jsonObject.get("genre").getAsString();

        ArrayList<Book> books;
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            EditBooksTable ebt = new EditBooksTable();
            if (genre.equals("All")) {
                books = ebt.databaseToBooks();

            } else {
                books = ebt.databaseToBooks(genre);
            }
            if (books == null) {

                response.setStatus(403);
            } else {
                String json = ebt.bookToJSON(books);
                out.println(json);
                response.setStatus(200);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GetBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
