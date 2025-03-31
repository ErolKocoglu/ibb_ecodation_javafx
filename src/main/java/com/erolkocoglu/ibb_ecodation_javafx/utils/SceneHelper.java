package com.erolkocoglu.ibb_ecodation_javafx.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHelper {
    ///
    public static void switchScene(String fxmlPath, Node currentNode, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneHelper.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) currentNode.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (
                IOException e) {
            showError("Hata FXML Yüklenemedi", "Dosya Yolu!", Alert.AlertType.ERROR);
        }
    }

    ///
    private static void showError(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
