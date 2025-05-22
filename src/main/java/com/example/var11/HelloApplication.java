package com.example.var11;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    private static final int CELL_SIZE = 20;
    private static final int SMALL_GRID_SIZE = 16;
    private static final int LARGE_GRID_SIZE = 32;

    private Color selectedColor = Color.BLACK;
    private Rectangle[][] grid;
    private GridPane gridPane;
    private ComboBox<String> gridSizeComboBox;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        HBox controlPanel = createControlPanel();

        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        createGrid(SMALL_GRID_SIZE);

        root.getChildren().addAll(controlPanel, gridPane);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Pixel Art Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createControlPanel() {
        HBox controlPanel = new HBox(10);

        ColorPicker colorPicker = new ColorPicker(selectedColor);
        colorPicker.setOnAction(e -> selectedColor = colorPicker.getValue());

        Button clearButton = new Button("Очистить");
        clearButton.setOnAction(e -> clearGrid());

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> saveImage());

        gridSizeComboBox = new ComboBox<>();
        gridSizeComboBox.getItems().addAll("16x16", "32x32");
        gridSizeComboBox.setValue("16x16");
        gridSizeComboBox.setOnAction(e -> changeGridSize());

        controlPanel.getChildren().addAll(
                new Label("Цвет:"), colorPicker,
                new Label("Размер сетки:"), gridSizeComboBox,
                clearButton, saveButton
        );

        return controlPanel;
    }

    private void createGrid(int size) {
        gridPane.getChildren().clear();
        grid = new Rectangle[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE, Color.WHITE);
                rect.setStroke(Color.LIGHTGRAY);

                rect.setOnMouseClicked(e -> {
                    if (e.getButton().name().equals("PRIMARY")) {
                        rect.setFill(selectedColor);
                    }
                });

                grid[row][col] = rect;
                gridPane.add(rect, col, row);
            }
        }
    }

    private void clearGrid() {
        for (Rectangle[] row : grid) {
            for (Rectangle rect : row) {
                rect.setFill(Color.WHITE);
            }
        }
    }

    private void changeGridSize() {
        String selectedSize = gridSizeComboBox.getValue();
        if (selectedSize.equals("16x16")) {
            createGrid(SMALL_GRID_SIZE);
        } else {
            createGrid(LARGE_GRID_SIZE);
        }
    }

    private void saveImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить изображение");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG файлы", "*.png"));

        File file = fileChooser.showSaveDialog(gridPane.getScene().getWindow());
        if (file != null) {
            try {
                WritableImage image = gridPane.snapshot(null, null);

                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                showAlert("Ошибка сохранения", "Не удалось сохранить изображение.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
