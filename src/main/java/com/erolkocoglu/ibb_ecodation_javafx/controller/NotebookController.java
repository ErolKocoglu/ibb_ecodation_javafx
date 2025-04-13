package com.erolkocoglu.ibb_ecodation_javafx.controller;

import com.erolkocoglu.ibb_ecodation_javafx.dao.NotebookDAO;
import com.erolkocoglu.ibb_ecodation_javafx.dto.KdvDTO;
import com.erolkocoglu.ibb_ecodation_javafx.dto.NotebookDTO;

import com.erolkocoglu.ibb_ecodation_javafx.utils.FXMLPath;
import com.erolkocoglu.ibb_ecodation_javafx.utils.SpecialColor;
import com.erolkocoglu.ibb_ecodation_javafx.utils.StyleMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class NotebookController {
    private NotebookDAO notebookDAO;

    public NotebookController() {
        notebookDAO = new NotebookDAO();
    }

    @FXML
    private TableView<NotebookDTO> notebookTable;
    @FXML private TableColumn<NotebookDTO, Long> idColumn;
    @FXML private TableColumn<NotebookDTO, String> titleColumn;
    @FXML private TableColumn<NotebookDTO, String> contentColumn;
    @FXML private TableColumn<NotebookDTO, Date> createdDatedColumn;
    @FXML private TableColumn<NotebookDTO, Date> updatedDateColumn;
    @FXML private TableColumn<NotebookDTO, String> categoryColumn;
    @FXML private TableColumn<NotebookDTO, Boolean> pinnedColumn;
    @FXML private Label notebookHeader;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        createdDatedColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        updatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("updatedDate"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        pinnedColumn.setCellValueFactory(new PropertyValueFactory<>("pinned"));

        refreshTable();
    }


    @FXML
    public void refreshTable() {
        Optional<List<NotebookDTO>> notebookDTOList =notebookDAO.list(Long.valueOf(UserSession.getCurrentUser().getId()));
        List<NotebookDTO> notebookDTOs = notebookDTOList.orElseGet(List::of);
        ObservableList<NotebookDTO> observableList = FXCollections.observableArrayList(notebookDTOs);
        notebookTable.setItems(observableList);
        showAlert("Bilgi", "Tablo başarıyla yenilendi!", Alert.AlertType.INFORMATION);

    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void addNote(ActionEvent event) {
        NotebookDTO newNotebookDTO = showNotebookForm(null);
        if (newNotebookDTO != null) {
            newNotebookDTO.setUserId(UserSession.getCurrentUser().getId());
            notebookDAO.create(newNotebookDTO);
            refreshTable();
        }
    }

    private NotebookDTO showNotebookForm(NotebookDTO existing) {
        Dialog<NotebookDTO> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Yeni Note Ekle" : "Not Güncelle");

        TextField titleField = new TextField();
        TextField contentField = new TextField();
        TextField categoryField = new TextField();
        ComboBox<Boolean> pinnedField = new ComboBox<>();
        pinnedField.getItems().addAll(true, false);
        pinnedField.setValue(false);

        if (existing != null) {
            titleField.setText(String.valueOf(existing.getTitle()));
            contentField.setText(String.valueOf(existing.getContent()));
            categoryField.setText(existing.getCategory());
            pinnedField.setValue(existing.isPinned());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, new Label("Başlık:"), titleField);
        grid.addRow(1, new Label("İçerik"), contentField);
        grid.addRow(2, new Label("Kategori"), categoryField);
        grid.addRow(3, new Label("Pinned:"), pinnedField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return NotebookDTO.builder()
                            .title(titleField.getText())
                            .content(contentField.getText())
                            .category(categoryField.getText())
                            .pinned(pinnedField.getValue())
                            .build();
                } catch (Exception e) {
                    showAlert("Hata", "Geçersiz veri!", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<NotebookDTO> result = dialog.showAndWait();
        return result.orElse(null);
    }

    @FXML
    public void updateNote(ActionEvent event) {
        NotebookDTO selected = notebookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Uyarı", "Güncellenecek bir kayıt seçin.", Alert.AlertType.WARNING);
            return;
        }

        NotebookDTO updated = showNotebookForm(selected);
        if (updated != null) {
            notebookDAO.update(Math.toIntExact(selected.getId()), updated);
            refreshTable();
            showAlert("Başarılı", "Not kaydı güncellendi.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void deleteNote(ActionEvent event) {
        NotebookDTO selected = notebookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Uyarı", "Silinecek bir kayıt seçin.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Silmek istiyor musunuz?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Not: " + selected.getTitle());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            notebookDAO.delete(Math.toIntExact(selected.getId()));
            refreshTable();
            showAlert("Silindi", "Not kaydı silindi.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void home(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLPath.ADMIN));
            Parent parent = fxmlLoader.load();

            Stage stage = (Stage) notebookHeader.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Admin Panel");
            stage.getScene().getStylesheets().add(getClass().getResource(StyleMode.currentStyle).toExternalForm());
            stage.show();
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Admin Sayfasına yönlendirme başarısız" + SpecialColor.RESET);
            e.printStackTrace();
        }
    }


}
