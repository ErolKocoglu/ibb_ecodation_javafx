package com.erolkocoglu.ibb_ecodation_javafx.controller;

import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;
import com.erolkocoglu.ibb_ecodation_javafx.utils.FXMLPath;
import com.erolkocoglu.ibb_ecodation_javafx.utils.SpecialColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

;
import java.util.Optional;

public class ProfileController {

    @FXML private TextField id;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private TextField role;
    @FXML private Button backButton;



    @FXML
    public void initialize() {
        id.setText(UserSession.getCurrentUser().getId().toString());
        username.setText(UserSession.getCurrentUser().getUsername());
        email.setText(UserSession.getCurrentUser().getEmail());
        role.setText(UserSession.getCurrentUser().getRole().toString());
        backButton.setText("Back");

    }

    @FXML
    private void backToAdmin(ActionEvent event) {//Admin sayfasına geri dönmek için
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.ADMIN));
            Parent parent = fxmlLoader.load();

            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Admin Panel");
            stage.show();
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Admin Sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
        }
    }
}
