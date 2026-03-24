package org.example.concertjavafx.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.concertjavafx.dao.SongDAO;
import org.example.concertjavafx.dao.VoteDAO;
import org.example.concertjavafx.entity.Song;
import org.example.concertjavafx.entity.User;
import org.example.concertjavafx.entity.Vote;

import java.io.IOException;

public class MainFrame {
    @FXML private TableView<Song> songTable;
    @FXML private TableColumn<Song, String> nameCol;
    @FXML private TableColumn<Song, String> artistCol;
    @FXML private TableColumn<Song, Integer> votesCol;

    private final SongDAO songDAO = new SongDAO();
    private final VoteDAO voteDAO = new VoteDAO();
    private static User currentUser;

    public MainFrame() {}

    public void show(User user, Stage stage) {
        currentUser = user;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/concertjavafx/main-view.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Концерт — " + user.getLogin());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        votesCol.setCellValueFactory(new PropertyValueFactory<>("voteCount"));
        refreshData();
    }

    @FXML
    private void refreshData() {
        songTable.setItems(FXCollections.observableArrayList(songDAO.findAll()));
    }

    @FXML
    private void handleVote() {
        Song selected = songTable.getSelectionModel().getSelectedItem();
        if (selected != null && currentUser != null) {
            // Оновлення лічильника пісні
            selected.incrementVoteCount();
            songDAO.save(selected);

            // Фіксація голосу в базі
            Vote vote = new Vote();
            vote.setUser(currentUser);
            vote.setSong(selected);
            voteDAO.save(vote);

            refreshData();
        }
    }
}