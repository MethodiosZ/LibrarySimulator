/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import mainClasses.Librarians;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class EditLibrarianTable {

    public Librarians addLibrarianFromJSON(String json) throws ClassNotFoundException {
        Librarians lib = jsonToLibrarian(json);
        addNewLibrarian(lib);
        return lib;
    }

    public Librarians jsonToLibrarian(String json) {
        Gson gson = new Gson();

        Librarians lib = gson.fromJson(json, Librarians.class);
        lib.setLat(0.0);
        lib.setLon(0.0);
        return lib;
    }

    public String librarianToJSON(Librarians lib) {
        Gson gson = new Gson();

        String json = gson.toJson(lib, Librarians.class);
        return json;
    }

    public void updateLibrarian(String username, String personalpage) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET personalpage='" +  personalpage + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarian(Librarians lib) throws SQLException, ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String update = "UPDATE librarians SET  "
                    + "(email,password,firstname,lastname,birthdate,gender,country,city,address,"
                    + "libraryname,libraryinfo,lat,lon,telephone,personalpage)"
                    + " VALUES ("
                    + "'" + lib.getEmail() + "',"
                    + "'" + lib.getPassword() + "',"
                    + "'" + lib.getFirstname() + "',"
                    + "'" + lib.getLastname() + "',"
                    + "'" + lib.getBirthdate() + "',"
                    + "'" + lib.getGender() + "',"
                    + "'" + lib.getCountry() + "',"
                    + "'" + lib.getCity() + "',"
                    + "'" + lib.getAddress() + "',"
                    + "'" + lib.getLibraryname() + "',"
                    + "'" + lib.getLibraryinfo() + "',"
                    + "'" + lib.getLat() + "',"
                    + "'" + lib.getLon() + "',"
                    + "'" + lib.getTelephone() + "',"
                    + "'" + lib.getPersonalpage() + "'"
                    + ")  WHERE username = '" + lib.getUsername() + "'";
            stmt.executeUpdate(update);

        } catch (SQLException ex) {
            Logger.getLogger(EditLibrarianTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printLibrarianDetails(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE username = '" + username + "' AND password='" + password + "'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public Librarians databaseToLibrarian(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE username = '" + username + "' AND password='" + password + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarians lib = gson.fromJson(json, Librarians.class);
            return lib;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public JsonArray databaseToLibrarianJson() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        JsonArray ja = new JsonArray();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians");
            while (rs.next()) {
                JsonObject json = DB_Connection.getResultsToJSONObject(rs);
                Gson gson = new Gson();
                ja.add(json);
            }
            return ja;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Librarians> databaseToLibrarians() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Librarians> librarians = new ArrayList<Librarians>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Librarians lib = gson.fromJson(json, Librarians.class);
                librarians.add(lib);
            }
            return librarians;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


    public void createLibrariansTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE librarians"
                + "(library_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(200) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(30) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    country VARCHAR(30) not null,"
                + "    city VARCHAR(50) not null,"
                + "    address VARCHAR(50) not null,"
                + "    libraryname VARCHAR(100) not null,"
                + "    libraryinfo VARCHAR(1000) not null,"
                + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + "    telephone VARCHAR(14),"
                + "    personalpage VARCHAR(200),"
                + " PRIMARY KEY (library_id))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewLibrarian(Librarians lib) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " librarians (username,email,password,firstname,lastname,birthdate,gender,country,city,address,"
                    + "libraryname,libraryinfo,lat,lon,telephone,personalpage)"
                    + " VALUES ("
                    + "'" + lib.getUsername() + "',"
                    + "'" + lib.getEmail() + "',"
                    + "'" + lib.getPassword() + "',"
                    + "'" + lib.getFirstname() + "',"
                    + "'" + lib.getLastname() + "',"
                    + "'" + lib.getBirthdate() + "',"
                    + "'" + lib.getGender() + "',"
                    + "'" + lib.getCountry() + "',"
                    + "'" + lib.getCity() + "',"
                    + "'" + lib.getAddress() + "',"
                      + "'" + lib.getLibraryname()+ "',"
                   + "'" + lib.getLibraryinfo()+ "',"
                    + "'" + lib.getLat() + "',"
                    + "'" + lib.getLon() + "',"
                    + "'" + lib.getTelephone() + "',"
                   + "'" + lib.getPersonalpage()+ "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The libarian was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditLibrarianTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
