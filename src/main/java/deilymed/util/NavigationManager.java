package deilymed.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class NavigationManager {

    /**
     * Navega para uma nova tela FXML, fechando a tela atual.
     *
     * @param fxmlPath O caminho para o arquivo FXML da nova tela (ex: "/deilymed/view/home.fxml").
     * @param newStageTitle O título da janela da nova tela.
     * @param currentNode Um componente (Node) da tela atual, usado para obter o Stage e fechá-lo.
     */
    public static void navigateTo(String fxmlPath, String newStageTitle, Node currentNode) {
        try {
            // Carrega o FXML da nova tela
            Parent root = FXMLLoader.load(Objects.requireNonNull(NavigationManager.class.getResource(fxmlPath)));

            // Cria uma nova cena
            Scene scene = new Scene(root);

            // Cria um novo palco (Stage) para a nova tela
            Stage stage = new Stage();
            stage.setTitle(newStageTitle);
            stage.setScene(scene);
            stage.setResizable(false);

            // Mostra a nova tela
            stage.show();

            // Obtém e fecha a tela atual usando o Node fornecido
            Stage stageAtual = (Stage) currentNode.getScene().getWindow();
            stageAtual.close();

        } catch (IOException | NullPointerException e) {
            // Utiliza o método de alerta centralizado
            mostrarAlertaDeErro("Erro de Navegação", "Não foi possível carregar a tela. Por favor, contate o suporte.\nCaminho: " + fxmlPath);
            e.printStackTrace(); // Mantém para depuração no console
        }
    }

    /**
     * Exibe um pop-up de alerta de erro genérico.
     *
     * @param titulo O título da janela de alerta.
     * @param mensagem A mensagem a ser exibida no alerta.
     */
    public static void mostrarAlertaDeErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Deixar o cabeçalho nulo para um visual mais limpo
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}