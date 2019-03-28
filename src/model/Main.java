package model;

import controller.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.Controller;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.listMovies();
        primaryStage.setTitle("Filmotéka");
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!Datasource.getInstance().open()) {
            System.out.println("ERROR: Spojení s databází selhalo!");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }

    public static void main(String[] args) {
            launch(args);


        }
    }

