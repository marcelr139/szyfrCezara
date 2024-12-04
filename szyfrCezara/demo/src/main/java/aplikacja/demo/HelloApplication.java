package aplikacja.demo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HelloApplication extends Application {

    private TextArea polezawartoscipliku;
    private TextArea poleedycji;
    private TextField sciezkadopliku;
    private TextField klucz;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Odczyt plików");

        sciezkadopliku = new TextField();
        sciezkadopliku.setPromptText("Ścieżka do pliku");

        klucz = new TextField();
        klucz.setPromptText("klucz");

        Button zaladuj = new Button("Wczytaj plik");
        zaladuj.setOnAction(e -> loadFile());
        
        Button zaszyfruj = new Button("zaszyfruj tekst");
        zaszyfruj.setOnAction(e -> zaszyfrujTekst());
        
        Button odszyfruj = new Button("odszyfruj tekst");
        odszyfruj.setOnAction(e -> odszyfrujTekst());

        polezawartoscipliku = new TextArea();
        polezawartoscipliku.setEditable(false);

        poleedycji = new TextArea();

        Button zapisz = new Button("Zapisz");
        zapisz.setOnAction(e -> saveFile());

        Button zamknij = new Button("Zamknij");
        zamknij.setOnAction(e -> primaryStage.close());

        VBox topLayout = new VBox(sciezkadopliku, zaladuj,klucz,zaszyfruj,odszyfruj);
        VBox bottomLayout = new VBox(poleedycji,polezawartoscipliku , zapisz, zamknij);
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topLayout);
        mainLayout.setCenter(bottomLayout);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void zaszyfrujTekst() {
        String tekstDoZaszyfrowania = poleedycji.getText();
        String kluczTekstowy = klucz.getText();
        int kluczSzyfrowania = Integer.parseInt(kluczTekstowy);
        String zaszyfrowanyTekst = zaszyfrujCezarem(tekstDoZaszyfrowania, kluczSzyfrowania);
        poleedycji.setText(zaszyfrowanyTekst);
        polezawartoscipliku.setText(zaszyfrowanyTekst);
    }

    private void odszyfrujTekst() {
        String tekstDoOdszyfrowania = poleedycji.getText();
        String kluczTekstowy = klucz.getText();
        int kluczSzyfrowania = Integer.parseInt(kluczTekstowy);
        String odszyfrowanyTekst = zaszyfrujCezarem(tekstDoOdszyfrowania, -kluczSzyfrowania);
        poleedycji.setText(odszyfrowanyTekst);
        polezawartoscipliku.setText(odszyfrowanyTekst);
    }

    private String zaszyfrujCezarem(String tekst, int przesuniecie) {
        char[] alfabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String wynik = "";

        for (char znak : tekst.toCharArray()) {
            int indeks = znajdzIndeksWAlfabecie(znak, alfabet);
            if (indeks != -1) {
                int nowyIndeks = (indeks + przesuniecie + alfabet.length / 2) % (alfabet.length / 2);
                if (nowyIndeks < 0) {
                    nowyIndeks += alfabet.length / 2;
                }
                if (Character.isUpperCase(znak)) {
                    nowyIndeks += alfabet.length / 2;
                }
                wynik += alfabet[nowyIndeks];
            } else {
                wynik += znak;
            }
        }
        return wynik;
    }

    private int znajdzIndeksWAlfabecie(char znak, char[] alfabet) {
        for (int i = 0; i < alfabet.length; i++) {
            if (alfabet[i] == znak) {
                return i;
            }
        }
        return 0;
    }

    private void loadFile() {
        String filePath = sciezkadopliku.getText();
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            try {
                List<String> fileContent = readFile(filePath);
                polezawartoscipliku.setText(String.join("\n", fileContent));
                poleedycji.setText(String.join("\n", fileContent));
            } catch (IOException e) {
                showAlert("Błąd", "Nie można odczytać pliku: " + e.getMessage());
            }
        } else {
            showAlert("Błąd", "Coś poszło nie tak.");
        }
    }

    private void saveFile() {
        String filePath = sciezkadopliku.getText();
        List<String> contentToSave = List.of(poleedycji.getText().split("\n"));
        try {
            writeFile(filePath, contentToSave);
            polezawartoscipliku.setText(poleedycji.getText());
            showAlert("Sukces", "Plik został zapisany.");
        } catch (IOException e) {
            showAlert("Błąd", "Nie można zapisać pliku: " + e.getMessage());
        }
    }

    private List<String> readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    private void writeFile(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, lines);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}