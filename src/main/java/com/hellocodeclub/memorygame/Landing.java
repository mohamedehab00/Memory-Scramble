package com.hellocodeclub.memorygame;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Landing {
    @FXML
    public TextField rows;
    @FXML
    public TextField cols;

    @FXML
    public TextField mins;

    @FXML
    public Button generate;

    public void generateClicked(MouseEvent mouseEvent) throws IOException {
        int nRows = Integer.parseInt(rows.getText());
        int nCols = Integer.parseInt(cols.getText());

        int res = nRows * nCols;

        if (res % 2 == 0 && nRows == nCols){
            AppController.nRows = nRows;
            AppController.nCols = nCols;
            int minutes = Integer.parseInt(mins.getText());
            AppController.timeRemaining = minutes*60;

            Stage stage = (Stage) generate.getScene().getWindow();
            stage.close();
            Stage primary = new Stage();
            URL url = new File("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\java\\com\\hellocodeclub\\memorygame\\main.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            primary.setTitle("Game");
            primary.setScene(new Scene(root));
            primary.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(minutes*60));
            delay.setOnFinished( event -> {
                primary.close();
                Stage primary1 = new Stage();
                URL url1 = null;
                try {
                    url1 = new File("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\java\\com\\hellocodeclub\\memorygame\\close.fxml").toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                Parent root1 = null;
                try {
                    root1 = FXMLLoader.load(url1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                primary1.setTitle("Game Over");
                primary1.setScene(new Scene(root1));
                primary1.show();
            } );
            delay.play();
        }
        else {
            System.out.println("No");
        }
    }
}
