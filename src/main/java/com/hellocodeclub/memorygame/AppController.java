package com.hellocodeclub.memorygame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

public class AppController {
    public static int nRows;
    public static int nCols;

    public static int timeRemaining = 60; // Initial time in seconds

    private Timeline timeline = null;

    public AppController() {
        board = new Board(nRows,nCols);
    }

    @FXML
    public GridPane gameMatrix;
    @FXML
    public Label countdownLabel;
    public Board board;

    Cell firstCard = null;
    Cell secondCard = null;

    @FXML
    public void initialize() throws FileNotFoundException {

        board.populateMatrix();

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                FileInputStream inputStream = new FileInputStream("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\resources\\static\\img\\question.jpg");
                ImageView imageView = getImageView(inputStream, i, j);
                gameMatrix.add(imageView,i,j);
            }
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            countdownLabel.setText("Time remaining: " + timeRemaining + " seconds");

            if (timeRemaining <= 0) {
                // Countdown finished
                countdownLabel.setText("Countdown finished!");
                timeline.stop();
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private ImageView getImageView(FileInputStream inputStream, int i, int j) {
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(90);
        imageView.setFitHeight(90);
        imageView.setUserData(i +" "+ j);
        imageView.setOnMouseClicked(mouseEvent -> {
            try {
                cardListener(mouseEvent);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return imageView;
    }

    private void cardListener(MouseEvent mouseEvent) throws IOException {
        Node source = (Node) mouseEvent.getSource();

        String rowAndColumn = (String) source.getUserData();

        int currRow = Integer.parseInt(rowAndColumn.split(" ")[0]);
        int currCol = Integer.parseInt(rowAndColumn.split(" ")[1]);

        String image = board.board[currRow][currCol].value;

        FileInputStream inputStream = new FileInputStream("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\resources\\static\\img\\"+image+".jpg");

        Image selectedImage = new Image(inputStream);
        ((ImageView)source).setImage(selectedImage);
        checkIfMatchingPairWasFound(currRow,currCol);
        if (isAllGuessed()){
            Stage stage = (Stage) gameMatrix.getScene().getWindow();
            stage.close();
            Stage primary = new Stage();
            URL url = new File("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\java\\com\\hellocodeclub\\memorygame\\win.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            primary.setTitle("Congrats");
            primary.setScene(new Scene(root));
            primary.show();
        }
    }

    public void checkIfMatchingPairWasFound(int rowSelected, int colSelected) throws FileNotFoundException {
        if (firstCard == null){
            firstCard = board.board[rowSelected][colSelected];
        } else if (secondCard == null) {
            secondCard = board.board[rowSelected][colSelected];
        }
        else {
            if (firstCard.value.equals(secondCard.value)){
                board.board[firstCard.row][firstCard.col].wasGuessed = true;
                board.board[secondCard.row][secondCard.col].wasGuessed = true;
            }
            else {
                int indexFirstCardInList = (firstCard.row * nRows) + firstCard.col ;
                FileInputStream inputStream = new FileInputStream("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\resources\\static\\img\\question.jpg");

                ((ImageView) gameMatrix.getChildren().get(indexFirstCardInList)).setImage(new Image(inputStream));

                int indexSecondCardInList = (secondCard.row * nRows) + secondCard.col ;
                inputStream = new FileInputStream("C:\\Users\\User\\Desktop\\memorygame\\src\\main\\resources\\static\\img\\question.jpg");

                ((ImageView) gameMatrix.getChildren().get(indexSecondCardInList)).setImage(new Image(inputStream));
            }
            firstCard = board.board[rowSelected][colSelected];
            secondCard = null;
        }
    }

    public boolean isAllGuessed(){
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (board.board[i][j].wasGuessed == false){
                    return false;
                }
            }
        }
        return true;
    }
}
