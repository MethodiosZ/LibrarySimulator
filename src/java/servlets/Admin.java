package servlets;

import com.google.gson.JsonArray;
import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;
import database.tables.EditBooksTable;
import database.tables.EditBooksInLibraryTable;
import mainClasses.Book;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Student;

/**
 *
 * @author Methodios
 */
public class Admin extends HttpServlet {

    @Override
    public void init() {
        try {
            Resources.setResources();
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String paramvalue = req.getHeader("reqtype");
            if (paramvalue.equals("users")) {
                res.setStatus(200);
                res.getWriter().write(UserstoJSON().toString());
            } else if (paramvalue.equals("chart1")) {
                res.setStatus(200);
                res.getWriter().write(BooksPerLibrary());
            } else if (paramvalue.equals("chart2")) {
                res.setStatus(200);
                res.getWriter().write(BooksPerGenre());
            } else if (paramvalue.equals("chart3")) {
                res.setStatus(200);
                res.getWriter().write(StudentsPerDegree());
            } else {
                res.setStatus(400);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
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
        handleRequest(request, response);
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

    }

    private JsonArray UserstoJSON() throws SQLException, ClassNotFoundException {
        EditStudentsTable ed = new EditStudentsTable();
        EditLibrarianTable lb = new EditLibrarianTable();
        JsonArray jsonArr = ed.databaseAllStudentToJSON();
        jsonArr.addAll(lb.databaseToLibrarianJson());
        return jsonArr;
    }

    private String BooksPerLibrary() throws SQLException, ClassNotFoundException {
        EditBooksInLibraryTable book = new EditBooksInLibraryTable();
        String data, out = "Quantity,Books per Library+";
        int count;
        for (int i = 1; i < 6; i++) {
            data = book.BooksInLibrary(i);
            count = data.length() - data.replace("{", "").length();
            switch(i){
                case 1:
                    out += "Vikelaia," + count + "+";
                    break;
                case 2:
                    out += "Vivliothiki Panepistimiou Kritis Iraklio," + count + "+";
                    break;
                case 3:
                    out += "Vivliothiki Panepistimiou Kritis Rethymno," + count + "+";
                    break;
                case 4:
                    out += "Vivliothiki Mesogeiako Panepistimio Iraklio," + count + "+";
                    break;
                case 5:
                    out += "Vivliothiki Politexneiou Chania," + count;
            }
        }
        return out;
    }

    private String BooksPerGenre() throws SQLException, ClassNotFoundException {
        EditBooksTable book = new EditBooksTable();
        ArrayList<Book> data;
        String out = "Quantity,Books per Genre+";
        data = book.databaseToBooks("Adventure");
        out += "Adventure," + data.size() + "+";
        data = book.databaseToBooks("Fantasy");
        out += "Fantasy," + data.size() + "+";
        data = book.databaseToBooks("Novel");
        out += "Novel," + data.size() + "+";
        data = book.databaseToBooks("Romance");
        out += "Romance," + data.size() + "+";
        data = book.databaseToBooks("Sports");
        out += "Sports," + data.size();
        return out;
    }

    private String StudentsPerDegree() throws SQLException, ClassNotFoundException {
        EditStudentsTable student = new EditStudentsTable();
        ArrayList<Student> data;
        String out = "Quantity,Students per Degree+";
        data = student.databaseStudentToString("null");
        out += "Undergraduate," + data.size() + "+";
        data = student.databaseStudentToString("BSc");
        out += "Postgraduate," + data.size() + "+";
        data = student.databaseStudentToString("PhD");
        out += "Doctoral," + data.size();
        return out;
    }
}
