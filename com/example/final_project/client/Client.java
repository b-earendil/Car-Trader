package com.example.final_project.client;

import com.example.final_project.model.Car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private UI ui;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client (){
    }

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }

    private void buildStringForServer(List<String> carQueryList) throws IOException, InterruptedException {
        String make = carQueryList.get(0);
        String model = carQueryList.get(1);
        String yearFrom = carQueryList.get(2);
        String yearTo = carQueryList.get(3);
        String minPrice = carQueryList.get(4);
        String maxPrice = carQueryList.get(5);

        String delimitedStringToSendToServer = make + "#" + model + "#" + yearFrom + "#" + yearTo + "#" + minPrice + "#" + maxPrice;
        communicateWithServer(delimitedStringToSendToServer);
    }

    private void communicateWithServer(String delimitedStringToSendToServer) throws IOException {
        // output
        System.out.println("Sending to server: ");
        out.println(delimitedStringToSendToServer);

        // input
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String receivedStringFromServer = in.readLine();
        ArrayList<Car> carList = unpackString(receivedStringFromServer);

        ui.showCars(carList);
    }

    public ArrayList<Car> unpackString(String receivedStringFromServer) {
        String delimeters = "[#]";
        String[] parsedStringFromServer = receivedStringFromServer.split(delimeters);

        ArrayList<Car> carResults = new ArrayList<>();
        for (int i = 0; i <= parsedStringFromServer.length - 3; i++) {
            Car car = new Car();
            car.setMake(parsedStringFromServer[i++]);
            car.setModel(parsedStringFromServer[i++]);
            car.setYear(Integer.parseInt(parsedStringFromServer[i++]));
            car.setPrice(Integer.parseInt(parsedStringFromServer[i]));
            carResults.add(car);
        }
        return carResults;
    }

    public void fetchCars(List<String> carQueryList) throws IOException, InterruptedException {
        // retrieve query from UI
        buildStringForServer(carQueryList);
    }
}
