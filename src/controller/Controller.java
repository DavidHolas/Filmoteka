package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import model.Actor;
import model.Movie;
import model.MyWriter;

import java.sql.SQLException;
import java.util.List;

public class Controller {

    @FXML
    private TextField movieNametf;

    @FXML
    private TextField movieGenretf;

    @FXML
    private TextField movieYeartf;

    @FXML
    private TextField actorNametf;

    @FXML
    private TextField actorYeartf;

    @FXML
    private TextField actorRelationshiptf;

    @FXML
    private TextField movieRelationshiptf;

    @FXML
    private TableView movieTable;

    public void listMovies() {
        Task<ObservableList<Movie>> task = new GetAllMoviesTask();
        movieTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    public void listActors() {
        Task<ObservableList<Actor>> task = new GetAllActorsTask();
        movieTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    public void moviesToXML() {
        List<Movie> movies = Datasource.getInstance().queryMovies();
        MyWriter myWriter = new MyWriter();
        myWriter.writeMoviesToXML(movies);
    }

    public void actorsToXML() {
        List<Actor> actors = Datasource.getInstance().queryActors();
        MyWriter myWriter = new MyWriter();
        myWriter.writeActorsToXML(actors);
    }

    public void endProgram() {
        System.out.println("Měj hezký den!");
        System.exit(0);
    }

    public void addMovieButtonClicked() {

        String name = movieNametf.getText();
        String genre = movieGenretf.getText();
        int year = Integer.parseInt(movieYeartf.getText());
        try {
            int result = Datasource.getInstance().insertMovie(name, genre, year);
            System.out.println(result);
        } catch(Exception e) {
           e.printStackTrace();
           System.out.println("Vložení filmu se nezdařilo. " + e.getMessage());
        }

        movieNametf.setText("");
        movieGenretf.setText("");
        movieYeartf.setText("");

    }

    public void addActorButtonClicked() {

        String name = actorNametf.getText();
        int year = Integer.parseInt(actorYeartf.getText());

        try {
            int result = Datasource.getInstance().insertActor(name, year);
            System.out.println(result);
        } catch(SQLException e) {
            System.out.println("Vložení herce se nezdařilo. " + e.getMessage());
            e.printStackTrace();
        }

        actorNametf.setText("");
        actorYeartf.setText("");
    }

    public void addRelationshipButtonClicked() {

        String actor = actorRelationshiptf.getText();
        String movie = movieRelationshiptf.getText();

        Datasource.getInstance().addActorToMovie(actor, movie);

        actorRelationshiptf.setText("");
        movieRelationshiptf.setText("");
    }


}

class GetAllMoviesTask extends Task {

    @Override
    protected ObservableList<Movie>  call() {
        return FXCollections.observableArrayList(Datasource.getInstance().queryMovies());
    }
}

class GetAllActorsTask extends Task {

    @Override
    protected ObservableList<Actor> call() {
        return FXCollections.observableArrayList(Datasource.getInstance().queryActors());
    }
}
