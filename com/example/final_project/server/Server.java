package com.example.final_project.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server {
    private ServerSocket server;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Server(ServerSocket server) throws IOException {
        this.server = server;
        System.out.println("server started, waiting for connection...");
        socket = server.accept();
        System.out.println("connection established, listening...");
        // input
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    void getStringFromClient() throws IOException {
            String stringFromClient;
            if ((stringFromClient = in.readLine()) != null) { // if or while ????
                unpackStringFromClient(stringFromClient);
            }
        }

    void unpackStringFromClient(String stringFromClient) {
        String delimeters = "[#]";
        String[] parsedStringFromClient = stringFromClient.split(delimeters);
        generateSQLQuery(parsedStringFromClient);
    }

    private void generateSQLQuery(String[] parsedStringFromClient) {
        String make = parsedStringFromClient[0];
        String model = parsedStringFromClient[1];
        String yearFrom = parsedStringFromClient[2];
        String yearTo = parsedStringFromClient[3];
        String minPrice = parsedStringFromClient[4];
        String maxPrice = parsedStringFromClient[5];

        String queryForDatabase = "SELECT make, model, year, price" + " FROM " + "car " + "WHERE " +
                "make" + " = " + "\"" + make + "\""  + " AND model = " + "\"" + model + "\"" +
                    " AND year BETWEEN " + yearFrom + " AND " +
                        yearTo + " AND price BETWEEN " + minPrice + " AND " + maxPrice + " ORDER BY year, price;" ;
        System.out.println("query for database:");
        System.out.println(queryForDatabase);
        queryDatabase(queryForDatabase);
    }

    private void queryDatabase(String queryForDatabase) {
        // try with resources, auto-closeable
        try(// create connection with database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/jid", "root", "root");
            // create a statement object to query the database
            Statement statement = conn.createStatement()) {
            // create table if it does not already exist
            // can improve project later by having table and
            // records inserted here rather than manually in MySql

            /*statement.execute("create table if not exists car (" +
                    " idCar int," +
                    " vin char(17)," +
                    " make varchar(25)," +
                    " model varchar(25)," +
                    " year int," +
                    " price int," +
                    " mileage int," +
                    " color varchar(20)," +
                    "primary key (idCar)" +
                    ")");*/

            ResultSet resultSet = statement.executeQuery(queryForDatabase);
            System.out.println("received car results from database");
            constructResultStringToSendToClient(resultSet);
        } catch(SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    private void constructResultStringToSendToClient(ResultSet resultSet) throws SQLException {
        String make = null;
        String model = null;
        String year = null;
        String price = null;
        String carResultStringForClient = "";
        // get records
        while(resultSet.next()){
            make = resultSet.getString(1);
            model = resultSet.getString(2);
            year = resultSet.getString(3);
            price = resultSet.getString(4);
            carResultStringForClient += make + "#" + model + "#" + year + "#" + price + "#";
        }
        returnStringFromDBToClient(carResultStringForClient);
    }

    private void returnStringFromDBToClient(String carResultStringForClient) {
        System.out.println("sending car results to client");
        System.out.println("server terminating..."); // Does server terminate here? Should this go elsewhere?
        out.println(carResultStringForClient);
    }

        public static void main(String[] args) throws IOException {
        Server server = new Server(new ServerSocket(8080));
        server.getStringFromClient();
    }
}
