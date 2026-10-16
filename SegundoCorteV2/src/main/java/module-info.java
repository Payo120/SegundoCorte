module me.edu.uam.segundocortev2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens me.edu.uam.segundocortev2 to javafx.fxml, javafx.base;
    opens me.edu.uam.segundocortev2.modelo to javafx.fxml, javafx.base;

    exports me.edu.uam.segundocortev2;
    exports me.edu.uam.segundocortev2.modelo;
    exports me.edu.uam.segundocortev2.dao;
}