package lk.ijse.pos_app.servlets;

import lk.ijse.pos_app.dto.CustomerDTO;
import lk.ijse.pos_app.dto.ItemDTO;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/pages/purchase-order")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            PreparedStatement statement = connection.prepareStatement("SELECT code FROM item");

            ResultSet resultSet = statement.executeQuery();

            resp.addHeader("Access-Control-Allow-Origin","*");

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            ItemDTO itemDTO = new ItemDTO();

            while (resultSet.next()){

                itemDTO.setCode(resultSet.getString(1));

                JsonObjectBuilder code = Json.createObjectBuilder();

                code.add("code",itemDTO.getCode());

                arrayBuilder.add(code.build());

            }

            PreparedStatement customerStatement = connection.prepareStatement("SELECT id FROM customer");
            ResultSet customerRT = customerStatement.executeQuery();

            CustomerDTO customerDTO = new CustomerDTO();

            while (customerRT.next()){

                customerDTO.setId(customerRT.getString(1));

                JsonObjectBuilder customer = Json.createObjectBuilder();

                customer.add("id",customerDTO.getId());

                arrayBuilder.add(customer.build());


            }

            resp.getWriter().print(arrayBuilder.build());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");

        JsonReader reader = Json.createReader(req.getReader());

        JsonObject jsonObject = reader.readObject();

        String id = jsonObject.getString("id");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            CustomerDTO customerDTO = new CustomerDTO();

            customerDTO.setId(id);

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM customer WHERE id=?");

            statement.setObject(1,customerDTO.getId());

            ResultSet resultSet = statement.executeQuery();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            while (resultSet.next()){

                String id_1 = resultSet.getString(1);

                String name = resultSet.getString(2);

                String address = resultSet.getString(3);

                String salary = String.valueOf(resultSet.getDouble(4));

                objectBuilder.add("id",id_1);
                objectBuilder.add("name",name);
                objectBuilder.add("address",address);
                objectBuilder.add("salary",salary);

            }

            resp.getWriter().print(objectBuilder.build());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");

        JsonReader reader = Json.createReader(req.getReader());

        JsonObject jsonObject = reader.readObject();

        String code = jsonObject.getString("code");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapp", "root", "1234");

            ItemDTO itemDTO = new ItemDTO();

            itemDTO.setCode(code);

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM item WHERE code=?");

            statement.setObject(1,itemDTO.getCode());

            ResultSet resultSet = statement.executeQuery();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            while (resultSet.next()){

                String code_1 = resultSet.getString(1);

                String name = resultSet.getString(2);

                String qty = String.valueOf(resultSet.getInt(3));

                String price = String.valueOf(resultSet.getDouble(4));

                objectBuilder.add("code",code_1);

                objectBuilder.add("name",name);

                objectBuilder.add("qty",qty);

                objectBuilder.add("price",price);

            }

            resp.getWriter().print(objectBuilder.build());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers","content-type");

    }
}
