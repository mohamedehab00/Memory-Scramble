package com.hellocodeclub.memorygame;

import javafx.fxml.FXML;

import java.util.Random;

public class Board {
    int nRows;
    int nCols;
    public Cell[][] board;

    public Board(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        board = new Cell[nRows][nCols];
    }

    public void populateMatrix(){
        board = new Cell[nRows][nCols];

        String[] images = {"apple","circle","heart","diamond","clover"};

        Random randomGenerator = new Random();

        while (!isBoardFull()){
            int randomImageIndex = randomGenerator.nextInt(images.length);

            String randomImageSelected = images[randomImageIndex];

            int randomRow1 = randomGenerator.nextInt(nRows);
            int randomCol1 = randomGenerator.nextInt(nCols);

            while (board[randomRow1][randomCol1] != null){
                randomRow1 = randomGenerator.nextInt(nRows);
                randomCol1 = randomGenerator.nextInt(nCols);
            }

            int randomRow2 = randomGenerator.nextInt(nRows);
            int randomCol2 = randomGenerator.nextInt(nCols);

            while (board[randomRow2][randomCol2] != null || (randomCol1 == randomCol2 && randomRow1 == randomRow2)){
                randomRow2 = randomGenerator.nextInt(nRows);
                randomCol2 = randomGenerator.nextInt(nCols);
            }

            board[randomRow1][randomCol1] = new Cell(randomImageSelected,randomRow1,randomCol1);
            board[randomRow2][randomCol2] = new Cell(randomImageSelected,randomRow2,randomCol2);
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (board[i][j] == null){
                    return false;
                }
            }
        }
        return true;
    }
}
