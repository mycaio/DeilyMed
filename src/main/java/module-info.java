module deilymed.deilymed {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    // 'java.desktop' é geralmente transitivo de javafx, mas pode ser mantido se usar AWT/Swing
    requires java.desktop;
    requires org.xerial.sqlitejdbc;


    opens deilymed to javafx.fxml;
    opens deilymed.Controller to javafx.fxml;
    opens deilymed.view to javafx.fxml;


    exports deilymed to javafx.graphics;
    //exports deilymed.Controller to javafx.graphics;

}
