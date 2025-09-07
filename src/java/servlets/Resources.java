package servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;
import java.sql.SQLException;
import java.util.HashMap;
import mainClasses.Person;

/**
 *
 * @author MethodiosZach
 */
public class Resources {

    static HashMap<String, Person> registeredUsers = new HashMap<>();

    public static void setResources() throws SQLException, ClassNotFoundException {
        Person p = new Person("admin", "admin12*", "admin");
        registeredUsers.put(p.getUsername(), p);

        //add all the students
        EditStudentsTable ed = new EditStudentsTable();
        JsonArray jsonArr = ed.databaseAllStudentToJSON();

        for (JsonElement js : jsonArr) {
            JsonObject obj = js.getAsJsonObject();
            p = new Person(obj.get("username").getAsString(), obj.get("password").getAsString(), "student");
            registeredUsers.put(p.getUsername(), p);
        }

        //add all the librarians
        EditLibrarianTable lb = new EditLibrarianTable();
        jsonArr = lb.databaseToLibrarianJson();

        for (JsonElement js : jsonArr) {
            JsonObject obj = js.getAsJsonObject();
            p = new Person(obj.get("username").getAsString(), obj.get("password").getAsString(), "librarian");
            registeredUsers.put(p.getUsername(), p);
        }
    }

    public static void addUser(String username, String password, String type) {
        Person p = new Person(username, password, type);
        registeredUsers.put(username, p);
    }
}
