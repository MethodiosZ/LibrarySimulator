package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Methodios
 */
public class Login extends HttpServlet {

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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(true);
        if (Resources.registeredUsers.containsKey(username)) {
            if (Resources.registeredUsers.get(username).getPassword().equals(password)) {
                session.setAttribute("loggedIn", username);

                int activeUsers = 0;
                if (request.getServletContext().getAttribute("activeUsers") != null) {
                    activeUsers = (int) request.getServletContext().getAttribute("activeUsers");
                }
                request.getServletContext().setAttribute("activeUsers", activeUsers + 1);
                response.setStatus(200);
            } else {
                response.setStatus(403);
            }
        } else {
            response.setStatus(403);
        }
    }
}
