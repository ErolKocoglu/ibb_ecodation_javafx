package com.erolkocoglu.ibb_ecodation_javafx.controller;

import com.erolkocoglu.ibb_ecodation_javafx.dao.UserDAO;
import com.erolkocoglu.ibb_ecodation_javafx.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {
    // Injection
    // Injection
    // Veri tabanı işlemleri için)
    private UserDAO userDAO;

    // Parametresiz Constructor
    public AdminController() {
        userDAO = new UserDAO();
    }

    /// ////////////////////////////////////////////////////////////////////////////
    // Kullanıcı tablosunu ve Sutunlarını tanımlamak
    // Dikkat: TableView => import javafx.scene.control.TableView;
    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, Integer> idColumn;

    @FXML
    private TableColumn<UserDTO, String> usernameColumn;


    @FXML
    private TableColumn<UserDTO, String> emailColumn;


    @FXML
    private TableColumn<UserDTO, String> passwordColumn;

    /// ////////////////////////////////////////////////////////////////////////////
    /// Admin sayfası açılırken hazır olsun
    @FXML
    public void initialize() {
        // Tablo sütunlarını UserDTO nesnesinin ilgili alanlarına bağlıyoruz
        // Dikkat: buradaki id, username, email,password
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Şifre sütununu maskeleme özelliği ekleniyor
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setText(null);
                } else {
                    setText("******"); // Şifreleri gizlemek için yıldız işareti kullanılıyor
                }
            }
        });

        // Kullanıcı listesini tabloya yükleme
        refreshTable();
    }

    /// ////////////////////////////////////////////////////////////////////////
    // Kullanıcı listesini veritabanından alıp tabloyu günceller
    @FXML
    private void refreshTable() {
        // Kulalnıcı Listesini Optional olarak Al
        Optional<List<UserDTO>> optionalUsers = userDAO.list(); // Kullanıcı listesini getir

        // Optional al ve eğer boşsa bir liste kullan
        List<UserDTO> userDTOList = optionalUsers.orElseGet(List::of);

        // ObservableList Çevir
        ObservableList<UserDTO> userObservableList = FXCollections.observableArrayList(userDTOList);

        // Tabloyu Yükle
        userTable.setItems(userObservableList);

        // Show Alert
        showAlert("Bilgi", "Tablo başarıyla yenilendi!", Alert.AlertType.INFORMATION);
    }

    /// ////////////////////////////////////////////////////////////////////////
    // Kullanıcıya mesaj göstermek için genel bir metod
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /// ////////////////////////////////////////////////////////////////////////
    // Kullanıcı oturumu kapatma işlemi
    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Çıkış Yap");
        alert.setHeaderText("Oturumdan çıkmak istiyor musunuz?");
        alert.setContentText("Emin misiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        // Eğer kullanıcıdan OK gelmişse
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ibb_ecodation_javafx/view/Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) userTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert("Hata", "Giriş sayfasına yönlendirme başarısız!", Alert.AlertType.ERROR);
            }
        }
    }


    /// ////////////////////////////////////////////////////////////////////////////
    public void addUser(ActionEvent actionEvent) {
    }

    public void updateUser(ActionEvent actionEvent) {
    }

    public void deleteUser(ActionEvent actionEvent) {
    }
}
