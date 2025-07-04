package deilymed;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal extends Application {
    Stage janela;

    public void start(Stage stage) throws Exception{
        janela = stage;

        Parent tela = FXMLLoader.load(getClass().getResource("view/home.fxml"));

        Scene scene = new Scene(tela);
        janela.setScene(scene);
        janela.show();
    }
}
