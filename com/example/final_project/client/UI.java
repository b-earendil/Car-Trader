package com.example.final_project.client;

import com.example.final_project.model.Car;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

// private UI field (this is needed in Client i think) LATER
// showCars() DONE
// private field of type client (uninitialized) DONE
// initialize it in ctor DONE
// (later in main, create client obj, pass obj to UI ctor) -> inside ctor: client.set(ui) DONE
// display form displayForm() DONE
// button, fires event
// override handle method DONE
// get values from form.. (values)
// inside UI handler, call client.fetchCars(values)

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class UI extends Application  {
    private Client client;
    private TextArea textArea = new TextArea();
    private int countOfTimesEnteredSelectionHandler = 0;

    public UI() {}
    public UI(Client client) {
        this.client = client;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // implement start()
        Socket socket = new Socket("localhost", 8080);
         this.client = new Client(socket);
         this.client.setUI(this);

        //Create makeChoiceBox node
        ChoiceBox<String> makeChoiceBox = new ChoiceBox<>();
        makeChoiceBox.getItems().addAll("Ford", "Tesla", "Toyota", "Mitsubishi", "Honda");
        ChoiceBox<String> modelChoiceBox = new ChoiceBox<>();

        //Create yearFromChoiceBox node
        ChoiceBox<String> yearFromChoiceBox = new ChoiceBox<>();
        yearFromChoiceBox.getItems().addAll("1985", "1986", "1987", "1988", "1989",
                "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999",
                "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
                "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
                "2020", "2021");

        //Create yearToChoiceBox node
        ChoiceBox<String> yearToChoiceBox = new ChoiceBox<>();
        yearToChoiceBox.getItems().addAll("1985", "1986", "1987", "1988", "1989",
                "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999",
                "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
                "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
                "2020", "2021");

        // Create priceMinChoiceBox node
        ChoiceBox<String> priceMinChoiceBox = new ChoiceBox<>();
        priceMinChoiceBox.getItems().addAll("8000", "9000", "10000", "11000", "12000",
                "13000", "14000", "15000", "20000", "25000", "30000", "35000", "40000", "45000", "50000",
                "55000", "60000", "65000", "70000", "75000", "80000", "85000", "90000", "100000", "125000",
                "150000", "200000");

        // Create priceMaxChoiceBox node
        ChoiceBox<String> priceMaxChoiceBox = new ChoiceBox<>();
        priceMaxChoiceBox.getItems().addAll("8000", "9000", "10000", "11000", "12000",
                "13000", "14000", "15000", "20000", "25000", "30000", "35000", "40000", "45000", "50000",
                "55000", "60000", "65000", "70000", "75000", "80000", "85000", "90000", "100000", "125000",
                "150000", "200000");

        Button btAdd = new Button("Search");

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        class SelectionHandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e) {
                // handle selection event
                // user chooses make, causing model box to populate with
                // correct options
                countOfTimesEnteredSelectionHandler++;
                String makeSelection = makeChoiceBox.getValue();

                // handles case where user selects make1 and then
                // re-selects make2 (w.l.o.g for any subsequent make)
                // ensures model box does not populate with multiple
                // makes on re-selection from the user
                if (countOfTimesEnteredSelectionHandler > 1) {
                    modelChoiceBox.getItems().removeAll(modelChoiceBox.getItems());
                }

                if (makeSelection.equalsIgnoreCase("Ford")) {
                    modelChoiceBox.getItems().addAll("Explorer", "F-150", "Fiesta", "Fusion");
                } else if (makeSelection.equalsIgnoreCase("Honda")) {
                    modelChoiceBox.getItems().addAll("Accord", "Civic", "CR-V", "Ridegeline");
                } else if (makeSelection.equalsIgnoreCase("Mitsubishi")){
                    modelChoiceBox.getItems().addAll("Eclipse", "Lancer", "Mirage", "Outlander");
                } else if (makeSelection.equalsIgnoreCase("Tesla")) {
                    modelChoiceBox.getItems().addAll("3", "S", "X", "Y", "Roadster");
                } else if (makeSelection.equalsIgnoreCase("Toyota")) {
                    modelChoiceBox.getItems().addAll("Camry", "Corolla", "Tacoma", "Tundra");
                }
            }
        }

        class BtnHandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e) {
                // handle button event
                // get values from form
                //  client.fetchCars(values);
                ArrayList<String> carSearchCriteriaList = new ArrayList<>();
                String make = makeChoiceBox.getValue();
                String model = modelChoiceBox.getValue();
                String yearFrom = yearFromChoiceBox.getValue();
                String yearTo = yearToChoiceBox.getValue();
                String minPrice = priceMinChoiceBox.getValue();
                String maxPrice = priceMaxChoiceBox.getValue();

                carSearchCriteriaList.add(make);
                carSearchCriteriaList.add(model);
                carSearchCriteriaList.add(yearFrom);
                carSearchCriteriaList.add(yearTo);
                carSearchCriteriaList.add(minPrice);
                carSearchCriteriaList.add(maxPrice);

                try {
                    client.fetchCars(carSearchCriteriaList);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Set event handlers
        makeChoiceBox.setOnAction(new SelectionHandler());
        btAdd.setOnAction(new BtnHandler());

        // Place nodes in the pane
        pane.add(new Label("Make:"), 1, 0);
        pane.add(makeChoiceBox, 2, 0);
        pane.add(new Label("Model:"), 3, 0);
        pane.add(modelChoiceBox, 4, 0);
        pane.add(new Label("yearFrom:"), 1, 2);
        pane.add(yearFromChoiceBox, 2, 2 );
        pane.add(new Label("yearTo:"), 3, 2);
        pane.add(yearToChoiceBox, 4, 2);
        pane.add(new Label("priceMin:"), 1, 3);
        pane.add(priceMinChoiceBox, 2, 3);
        pane.add(new Label("priceMax:"), 3, 3);
        pane.add(priceMaxChoiceBox, 4, 3);
        pane.add(textArea, 0,4);

        pane.add(btAdd, 4, 4);
        GridPane.setHalignment(btAdd, HPos.RIGHT);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane);
        stage.setTitle("Filter Cars"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage
    }

    void showCars(ArrayList<Car> carResults) {
        // print car results to the textArea
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Car car : carResults) {
            sb.append(car.toString()).append("\n");
        }
        textArea.setText("Make\t\t" + "Model\t\t" + "Year\t\t" + "Price");
        textArea.appendText(sb.toString());
        textArea.appendText(("\n"));
    }


    public static void main(String[] args) {
        launch();
    }
}