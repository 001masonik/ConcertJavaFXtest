package org.example.concertjavafx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.concertjavafx.dao.SongDAO;
import org.example.concertjavafx.dao.UserDAO;
import org.example.concertjavafx.entity.Song;
import org.example.concertjavafx.entity.User;
import org.example.concertjavafx.gui.LoginFrame;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        initializeData();

        System.out.println("Запуск вікна входу...");
        new LoginFrame().show(primaryStage);
    }

    private void initializeData() {
        UserDAO userDAO = new UserDAO();
        SongDAO songDAO = new SongDAO();

        try {
            if (userDAO.findByLogin("admin") == null) {
                User admin = new User();
                admin.setLogin("admin");
                admin.setPassword("admin");
                admin.setRole("Admin");
                userDAO.save(admin);
                System.out.println(">>> Адміністратора (admin/admin) створено успішно!");
            } else {
                System.out.println(">>> Адміністратор вже існує в базі.");
            }

            List<Song> songs = songDAO.findAll();
            if (songs == null || songs.isEmpty()) {
                System.out.println(">>> Додавання тестових пісень...");

                Song song1 = new Song();
                song1.setName("Stefania");
                song1.setArtist("Kalush Orchestra");
                song1.setDuration(3);
                songDAO.save(song1);

                Song song2 = new Song();
                song2.setName("Shum");
                song2.setArtist("Go_A");
                song2.setDuration(3);
                songDAO.save(song2);

                Song song3 = new Song();
                song3.setName("Teresa & Maria");
                song3.setArtist("alyona alyona & Jerry Heil");
                song3.setDuration(3);
                songDAO.save(song3);

                System.out.println(">>> Пісні успішно додані.");
            }

        } catch (Exception e) {
            System.err.println("Помилка при ініціалізації даних: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}