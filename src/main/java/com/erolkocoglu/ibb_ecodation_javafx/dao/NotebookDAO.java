package com.erolkocoglu.ibb_ecodation_javafx.dao;

import com.erolkocoglu.ibb_ecodation_javafx.database.SingletonPropertiesDBConnection;
import com.erolkocoglu.ibb_ecodation_javafx.dto.NotebookDTO;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotebookDAO {
    private Connection connection;

    public NotebookDAO() {
        this.connection = SingletonPropertiesDBConnection.getInstance().getConnection();
    }

    public Optional<NotebookDTO> create(NotebookDTO notebookDTO) {
        String sql = "INSERT INTO notebook_table (title, content, createdDate, updatedDate, category, pinned, userId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, notebookDTO.getTitle());
            preparedStatement.setString(2, notebookDTO.getContent());
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()) );
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(5, notebookDTO.getCategory());
            preparedStatement.setBoolean(6, notebookDTO.isPinned());
            preparedStatement.setInt(7, notebookDTO.getUserId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        notebookDTO.setId((long) generatedKeys.getInt(1));

                        return Optional.of(notebookDTO);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    //Kullanıcının notlarını görmek için
    public Optional<List<NotebookDTO>> list(Long userId) {
        List<NotebookDTO> notebookDTOList = new ArrayList<>();
        String sql = "SELECT * FROM notebook_table WHERE userId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                notebookDTOList.add(mapToObjectDTO(resultSet));
            }
            return notebookDTOList.isEmpty() ? Optional.empty() : Optional.of(notebookDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<NotebookDTO> update(int id, NotebookDTO notebookDTO) {
        Optional<NotebookDTO> optionalUpdate = findById(id);
        if (optionalUpdate.isPresent()) {
            String sql = "UPDATE notebook_table SET title=?, content=?, updatedDate=?, category=?, pinned=?, WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, notebookDTO.getTitle());
                preparedStatement.setString(2, notebookDTO.getContent());
                preparedStatement.setDate(3, Date.valueOf(LocalDateTime.now().toLocalDate()) );
                preparedStatement.setString(4, notebookDTO.getCategory());
                preparedStatement.setBoolean(5, notebookDTO.isPinned());
                preparedStatement.setInt(6, id);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    notebookDTO.setId((long) id);
                    notebookDTO.setUpdatedDate(LocalDateTime.now());
                    return Optional.of(notebookDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public Optional<NotebookDTO> delete(int id) {
        Optional<NotebookDTO> optionalDelete = findById(id);
        if (optionalDelete.isPresent()) {
            String sql = "DELETE FROM notebook_table WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    return optionalDelete;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public Optional<NotebookDTO> findById(int id) {
        String sql = "SELECT * FROM notebook_table WHERE id=?";
        return selectSingle(sql, id);
    }

    public Optional<NotebookDTO> selectSingle(String sql, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject((i + 1), params[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapToObjectDTO(resultSet));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    public NotebookDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
        return NotebookDTO.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .content(resultSet.getString("content"))
                .createdDate(resultSet.getDate("createdDate").toLocalDate().atStartOfDay())
                .updatedDate(resultSet.getDate("updatedDate").toLocalDate().atStartOfDay())
                .category(resultSet.getString("category"))
                .pinned(resultSet.getBoolean("pinned"))
                .userId(resultSet.getInt("userId"))
                .build();
    }

}
