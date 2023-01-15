module com.example.earth3dtest {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jimObjModelImporterJFX;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.rmi;

    opens com.example.earth3dtest to javafx.fxml;
    exports com.example.earth3dtest;
    exports models to java.rmi;
}