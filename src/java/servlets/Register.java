package servlets;

import database.tables.EditLibrarianTable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.tables.EditStudentsTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.User;

/**
 *
 * @author MethodiosZach
 */
public class Register extends HttpServlet {

    /**
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        BufferedReader reader = req.getReader();
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(data);

        try {
            if (findType(data) == 1) {
                EditStudentsTable newstudent = new EditStudentsTable();
                User nst = newstudent.addStudentFromJSON(data);
                Resources.addUser(nst.getUsername(), nst.getPassword(), "student");
            } else {
                EditLibrarianTable newlibrarian = new EditLibrarianTable();
                User nlb = newlibrarian.addLibrarianFromJSON(data);
                Resources.addUser(nlb.getUsername(), nlb.getPassword(), "librarian");
            }
            res.setStatus(200);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int findType(String data) {
        int start = data.indexOf("usertype") + 11;
        if (data.startsWith("Student", start)) {
            return 1;
        }
        return 2;
    }
}
