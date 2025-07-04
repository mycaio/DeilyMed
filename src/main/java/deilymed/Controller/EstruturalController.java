
package deilymed.Controller;

import deilymed.util.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EstruturalController {
    @FXML
    private ImageView bt_sair;


    @FXML
    void sairDoModulo(MouseEvent event) {
        NavigationManager.navigateTo(
                "/deilymed/view/home.fxml", // 1. Para onde ir
                "Home",                     // 2. Título da nova janela
                bt_sair                     // 3. Um nó da tela atual para poder fechá-la
        );
    }
}

