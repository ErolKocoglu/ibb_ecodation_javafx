package com.erolkocoglu.ibb_ecodation_javafx.controller;

import com.erolkocoglu.ibb_ecodation_javafx.dao.UserDAO;
import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;
import com.erolkocoglu.ibb_ecodation_javafx.utils.ERole;
import com.erolkocoglu.ibb_ecodation_javafx.utils.FXMLPath;
import com.erolkocoglu.ibb_ecodation_javafx.utils.SpecialColor;
import com.erolkocoglu.ibb_ecodation_javafx.utils.StyleMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.Optional;

public class ProfileController {

    @FXML
    private TextField id;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private TextField role;
    @FXML
    private Button backButton;
    @FXML
    private VBox profileContainer;
    @FXML
    private Button editButton;

    private UserDAO userDAO;

    public ProfileController() {
        userDAO = new UserDAO();
    }


    @FXML
    public void initialize() {
        //Güvenlik açısından şifrenin yazılmasını gösterilmesini doğru bulmadım
        System.out.println("initialize worked");
        id.setText(UserSession.getCurrentUser().getId().toString());
        id.setDisable(true);
        username.setText(UserSession.getCurrentUser().getUsername());
        username.setDisable(true);
        email.setText(UserSession.getCurrentUser().getEmail());
        email.setDisable(true);
        role.setText(UserSession.getCurrentUser().getRole().toString());
        role.setDisable(true);
        //backButton.setText("Back");
        //username.getScene().getStylesheets().add(getClass().getResource("/com/erolkocoglu/ibb_ecodation_javafx/view/css/styles.css").toExternalForm());

    }

    @FXML
    private void backToAdmin(ActionEvent event) {//Admin sayfasına geri dönmek için
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.ADMIN));
            Parent parent = fxmlLoader.load();

            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Admin Panel");
            stage.getScene().getStylesheets().add(getClass().getResource(StyleMode.currentStyle).toExternalForm());
            stage.show();
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Admin Sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
        }
    }

    private static class UpdateUserDialog extends Dialog<UserDTO> {
        private final TextField usernameField = new TextField();
        private final PasswordField passwordField = new PasswordField();
        private final TextField emailField = new TextField();
        private final ComboBox<ERole> roleComboBox = new ComboBox<>();

        public UpdateUserDialog(UserDTO existingUser) {
            setTitle("Profil Güncelle");
            setHeaderText("Profilinizi düzenleyin");

            usernameField.setText(existingUser.getUsername());
            emailField.setText(existingUser.getEmail());

            // 🔥 ENUM kullanımıyla rol listesi
            roleComboBox.getItems().addAll(ERole.values());

            // 🔥 Mevcut role'u enum olarak seç
            try {
                roleComboBox.setValue(ERole.fromString(String.valueOf(existingUser.getRole())));
            } catch (RuntimeException e) {
                roleComboBox.setValue(ERole.USER); // Yedek: varsayılan rol
            }

            // Layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Kullanıcı Adı:"), 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(new Label("Yeni Şifre:"), 0, 1);
            grid.add(passwordField, 1, 1);
            grid.add(new Label("E-posta:"), 0, 2);
            grid.add(emailField, 1, 2);
            grid.add(new Label("Rol:"), 0, 3);
            grid.add(roleComboBox, 1, 3);

            getDialogPane().setContent(grid);

            ButtonType updateButtonType = new ButtonType("Güncelle", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Sonuç döndür
            setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    return UserDTO.builder()
                            .username(usernameField.getText().trim())
                            .password(passwordField.getText().trim().isEmpty()
                                    ? existingUser.getPassword()
                                    : passwordField.getText().trim())
                            .email(emailField.getText().trim())
                            .role(ERole.valueOf(roleComboBox.getValue().name())) // Enum’dan string’e dönüşüm
                            .build();
                }
                return null;
            });
        }
    }
    @FXML
    private void editProfile(ActionEvent event) {
        /*profileContainer.getChildren().clear();
        ComboBox<ERole> roles = new ComboBox<>();
        roles.getItems().addAll(ERole.values());
        roles.setValue(UserSession.getCurrentUser().getRole());
        roles.setStyle("-fx-padding: 8; -fx-background-radius: 8; -fx-border-radius: 8;");
        profileContainer.getChildren().add(roles);*/

        UserDTO selectedUser = UserSession.getCurrentUser();
        ProfileController.UpdateUserDialog dialog = new ProfileController.UpdateUserDialog(selectedUser);
        Optional<UserDTO> result = dialog.showAndWait();

        result.ifPresent(updatedUser -> {
            if (updatedUser.getUsername().isEmpty() || updatedUser.getPassword().isEmpty() || updatedUser.getEmail().isEmpty()) {
                showAlert("Hata", "Tüm alanlar doldurulmalı!", Alert.AlertType.ERROR);
                return;
            }

            Optional<UserDTO> updated = userDAO.update(selectedUser.getId(), updatedUser);
            if (updated.isPresent()) {
                showAlert("Başarılı", "Profil güncellendi!", Alert.AlertType.INFORMATION);
                UserSession.setCurrentUser(updated.get());
                initialize();
            } else {
                showAlert("Hata", "Güncelleme işlemi başarısız!", Alert.AlertType.ERROR);
            }
        });
    }

    /*@FXML
    private void changePassword(ActionEvent event) {

    }*/

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
