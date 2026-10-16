module me.edu.uam.segundocorte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens me.edu.uam.segundocorte to javafx.fxml, javafx.base;
    exports me.edu.uam.segundocorte;
}