package deilymed.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HomeController {

    @FXML
    private Button bt_agendamento;

    @FXML
    private Button bt_atendimento;

    @FXML
    private Button bt_estrutural;

    @FXML
    void abrirEstrutural(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/deilymed/view/estrutural.fxml")));

            // Cria uma nova cena
            Scene scene = new Scene(root);

            // Cria um novo palco (Stage) para a nova tela
            Stage stage = new Stage();
            stage.setTitle("Estrutural");
            stage.setScene(scene);
            stage.setResizable(false); // Boa prática para manter o layout consistente

            // Mostra a nova tela
            stage.show();

            // Fecha a tela atual (home) de forma segura
            Stage stageAtual = (Stage) bt_estrutural.getScene().getWindow();
            stageAtual.close();

        } catch (IOException | NullPointerException e) {
            //Alert para erro de camino
            mostrarAlertaDeErro("Erro de Navegação", "Não foi possível carregar a tela Estrutural. Por favor, contate o suporte.");
            e.printStackTrace(); // Mantemos para depuração
            System.out.println("ERRO" + e.getMessage());
        }
    }
    @FXML
    void abrirAtendimento(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/deilymed/view/atendimento.fxml")));

            // Cria uma nova cena
            Scene scene = new Scene(root);

            // Cria um novo palco (Stage) para a nova tela
            Stage stage = new Stage();
            stage.setTitle("Estrutural");
            stage.setScene(scene);
            stage.setResizable(false); // Boa prática para manter o layout consistente

            // Mostra a nova tela
            stage.show();

            // Fecha a tela atual (home) de forma segura
            Stage stageAtual = (Stage) bt_estrutural.getScene().getWindow();
            stageAtual.close();

        } catch (IOException | NullPointerException e) {
            mostrarAlertaDeErro("Erro de Navegação", "Não foi possível carregar a tela de Atendimento. Por favor, contate o suporte.");
            e.printStackTrace(); // Mantemos para depuração
            System.out.println("ERRO" + e.getMessage());
        }
    }

    @FXML
    void abrirAgendamento(ActionEvent event) {
        try {
          Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/deilymed/view/agendamento.fxml")));

            // Cria uma nova cena
            Scene scene = new Scene(root);

            // Cria um novo palco (Stage) para a nova tela
            Stage stage = new Stage();
            stage.setTitle("Estrutural");
            stage.setScene(scene);
            stage.setResizable(false); // Boa prática para manter o layout consistente

            // Mostra a nova tela
            stage.show();

            // Fecha a tela atual (home) de forma segura
            Stage stageAtual = (Stage) bt_estrutural.getScene().getWindow();
            stageAtual.close();

        } catch (IOException | NullPointerException e) {
            mostrarAlertaDeErro("Erro de Navegação", "Não foi possível carregar a tela de Agendamento. Por favor, contate o suporte.");
            e.printStackTrace(); // Mantemos para depuração
            System.out.println("ERRO" + e.getMessage());
        }
    }

    //Função para aler de erro de caminho
    private void mostrarAlertaDeErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Deixar o cabeçalho nulo para um visual mais limpo
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
