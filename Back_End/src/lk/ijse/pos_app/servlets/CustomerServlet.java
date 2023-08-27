package lk.ijse.pos_app.servlets;

import lk.ijse.pos_app.dto.CustomerDTO;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            Class.forName("com.mysql.cj.jdbc");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("select * from customer");

            ResultSet rst = pstm.executeQuery();

            // Create a JSON array builder to construct a JSON array to hold customer objects

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();

            // Loop through the result set and build JSON objects for each customer
            while (rst.next()) {

                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);

                JsonObjectBuilder customerObject = Json.createObjectBuilder();

                customerObject.add("id", id);
                customerObject.add("name", name);
                customerObject.add("address", address);

                allCustomers.add(customerObject.build());

            }

            // Respond to the HTTP request by writing the JSON array to the response
            resp.getWriter().print(allCustomers.build());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Retrieve customer data from request parameters
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        double cusSalary = Double.parseDouble(req.getParameter("cusAddress"));

        // Create a CustomerDTO object with the retrieved data
        CustomerDTO customerDTO = new CustomerDTO(cusID, cusName, cusAddress, cusSalary);

        // Set response headers for JSON content and cross-origin access
        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the MySQL database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            // Create a PreparedStatement for inserting customer data
            PreparedStatement pstm = connection.prepareStatement("insert into custommer values (?,?,?)");

            // Bind customer data to the prepared statement
            pstm.setObject(1, cusID);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);

            // Execute the INSERT query and check if any rows were affected
            if (pstm.executeUpdate() > 0) {

                // Create a JSON object to represent the response message
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                // Add status and message to the JSON response
                objectBuilder.add("state", "OK");
                objectBuilder.add("message", "Successfully Added!");
                objectBuilder.add("Data", ""); // This could be used for additional data

                // Send the JSON response to the client
                resp.getWriter().print(objectBuilder.build());
            }

        } catch (ClassNotFoundException | SQLException e) {
            // If an exception occurs, wrap it in a RuntimeException and throw it
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Create a JSON reader to parse the incoming JSON content
        JsonReader reader = Json.createReader(req.getReader());

        // Read the JSON object from the request
        JsonObject customerObject = reader.readObject();

        // Extract data from the JSON object
        String id = customerObject.getString("id");
        String name = customerObject.getString("name");
        String address = customerObject.getString("address");

        // Set response header for cross-origin access
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the MySQL database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            // Create a PreparedStatement for updating customer data
            PreparedStatement pstm = connection.prepareStatement("update customer set name=?, address=? where id=?");

            // Bind customer data to the prepared statement
            pstm.setObject(3, id);
            pstm.setObject(1, name);
            pstm.setObject(2, address);

            // Execute the UPDATE query and check if any rows were affected
            if (pstm.executeUpdate() > 0) {

                // Create a JSON object to represent the response message
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                // Add status and message to the JSON response
                objectBuilder.add("state", "OK");
                objectBuilder.add("message", "Successfully Updated!");
                objectBuilder.add("Data", ""); // This could be used for additional data

                // Send the JSON response to the client
                resp.getWriter().print(objectBuilder.build());

            }

        } catch (ClassNotFoundException | SQLException e) {
            // If an exception occurs, wrap it in a RuntimeException and throw it
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String id = jsonObject.getString("id");

        System.out.println(id);

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            resp.addHeader("Access-Control-Allow-Origin","*");

            PreparedStatement pstm = connection.prepareStatement("delete from customer where id=?");

            pstm.setObject(1,id);

            if (pstm.executeUpdate()>0){

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                objectBuilder.add("state","OK");
                objectBuilder.add("message","Successfully Deleted!");
                objectBuilder.add("Data","");

                resp.getWriter().print(objectBuilder.build());

            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Headers","content-type");

    }
}
