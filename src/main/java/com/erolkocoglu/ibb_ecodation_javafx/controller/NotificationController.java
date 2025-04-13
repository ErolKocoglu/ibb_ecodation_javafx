package com.erolkocoglu.ibb_ecodation_javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NotificationController {
    @FXML
    private TextArea notificationArea;

    public void setNotificationArea(TextArea notificationArea) {
        this.notificationArea = notificationArea;
    }

    private final String FILE_PATH = "bildirimler.txt";

    @FXML
    public void initialize() {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }
            notificationArea.setText(builder.toString());
        } catch (IOException e) {
            notificationArea.setText("Dosya okunamadı.");
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) notificationArea.getScene().getWindow();
        stage.close();
    }
}
