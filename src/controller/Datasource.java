package controller;

import javafx.scene.control.Alert;
import model.Movie;
import model.Actor;

import java.sql.*;
import java.util.*;

public class Datasource {

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost/filmoteka?serverTimezone=UTC";
    private static final String USER_NAME = "David";
    private static final String PASSWORD = "unicorn";

    private static final String TABLE_MOVIE = "filmy";
    private static final String COLUMN_MOVIE_ID = "idfilmy";
    private static final String COLUMN_MOVIE_NAME = "nazev";
    private static final String COLUMN_MOVIE_GENRE = "zanr";
    private static final String COLUMN_MOVIE_YEAR = "rok";
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_MOVIE_NAME = 2;
    private static final int INDEX_MOVIE_GENRE = 3;
    private static final int INDEX_MOVIE_YEAR = 4;

    private static final String TABLE_ACTOR = "herci";
    private static final String COLUMN_ACTOR_ID = "idherci";
    private static final String COLUMN_ACTOR_NAME = "jmeno";
    private static final String COLUMN_ACTOR_YEAR = "rok_narozeni";
    private static final int INDEX_ACTOR_ID = 1;
    private static final int INDEX_ACTOR_NAME = 2;
    private static final int INDEX_ACTOR_YEAR = 3;

    private static final String TABLE_ACTOR_MOVIE = "herci_filmy";

    private static final String QUERY_MOVIES_FOR_NAME = "SELECT * FROM " + TABLE_MOVIE +
            " WHERE " + COLUMN_MOVIE_NAME + " = ?";

    private static final String INSERT_MOVIE = "INSERT INTO " + TABLE_MOVIE +
            " (" + COLUMN_MOVIE_NAME + ", " + COLUMN_MOVIE_GENRE + ", " + COLUMN_MOVIE_YEAR +
            ") VALUES (?, ?, ?)";

    private static final String QUERY_ACTORS_FOR_NAME = "SELECT * FROM " + TABLE_ACTOR +
            " WHERE " + COLUMN_ACTOR_NAME + " = ?";

    private static final String INSERT_ACTOR = "INSERT INTO " + TABLE_ACTOR +
            " (" + COLUMN_ACTOR_NAME + ", " + COLUMN_ACTOR_YEAR + ") VALUES (?, ?)";

    private static final String INSERT_RELATIONSHIP = "INSERT INTO " + TABLE_ACTOR_MOVIE + " VALUES(?, ?)";

    public static final String ERROR_DATABASE = "Problém s databází!";
    public static final String ERROR_SOMETHING = "Něco se pokazilo!";


    private Connection conn;

    private PreparedStatement prepQueryMoviesForName;
    private PreparedStatement prepInsertMovie;
    private PreparedStatement prepQueryActorsForName;
    private PreparedStatement prepInsertActor;
    private PreparedStatement prepInsertRelationship;

    private static Datasource instance = new Datasource();

    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
            prepQueryMoviesForName = conn.prepareStatement(QUERY_MOVIES_FOR_NAME);
            prepInsertMovie = conn.prepareStatement(INSERT_MOVIE);
            prepQueryActorsForName = conn.prepareStatement(QUERY_ACTORS_FOR_NAME);
            prepInsertActor = conn.prepareStatement(INSERT_ACTOR);
            prepInsertRelationship = conn.prepareStatement(INSERT_RELATIONSHIP);
            return true;
        } catch (SQLException e) {
            System.out.println("Připojení k databázi se nezdařilo. " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (prepQueryMoviesForName != null) {
                prepQueryMoviesForName.close();
            }
            if (prepInsertMovie != null) {
                prepInsertMovie.close();
            }
            if (prepQueryActorsForName != null) {
                prepQueryActorsForName.close();
            }
            if (prepInsertActor != null) {
                prepInsertActor.close();
            }
            if (prepInsertRelationship != null) {
                prepInsertRelationship.close();
            }
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Uzavření spojení se nezdařilo. " + e.getMessage());
        }
    }

    public List<Movie> queryMovies() {

        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM " + TABLE_MOVIE + " ORDER BY " + COLUMN_MOVIE_NAME + " ASC")) {

            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt(INDEX_MOVIE_ID));
                movie.setName(rs.getString(INDEX_MOVIE_NAME));
                movie.setGenre(rs.getString(INDEX_MOVIE_GENRE));
                movie.setYear(rs.getInt(INDEX_MOVIE_YEAR));
                movies.add(movie);
            }

            return movies;

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba");
            alert.setHeaderText(ERROR_DATABASE);
            return null;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba");
            alert.setHeaderText(ERROR_SOMETHING);
            return null;
        }
    }

    public int insertMovie(String name, String genre, int year) throws SQLException {

        prepQueryMoviesForName.setString(1, name);
        ResultSet results = prepQueryMoviesForName.executeQuery();

        if (results.next()) {
            return results.getInt(1);
        } else {

            prepInsertMovie.setString(1, name);
            prepInsertMovie.setString(2, genre);
            prepInsertMovie.setInt(3, year);
            int affectedRows = prepInsertMovie.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Vložení filmu se nezdrařilo.");
            }
            return 0;

        }

    }

    public List<Actor> queryActors() {

        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM " + TABLE_ACTOR + " ORDER BY " + COLUMN_ACTOR_NAME + " ASC")) {

            List<Actor> actors = new ArrayList<>();
            while (rs.next()) {
                Actor actor = new Actor();
                actor.setId(rs.getInt(COLUMN_ACTOR_ID));
                actor.setName(rs.getString(COLUMN_ACTOR_NAME));
                actor.setYear(rs.getInt(COLUMN_ACTOR_YEAR));
                actors.add(actor);
            }

            return actors;

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba");
            alert.setHeaderText(ERROR_DATABASE);
            return null;
        }
    }

    public int insertActor(String name, int year) throws SQLException {

        prepQueryActorsForName.setString(1, name);
        ResultSet results = prepQueryActorsForName.executeQuery();

        if (results.next()) {
            return results.getInt(1);
        } else {

            prepInsertActor.setString(1, name);
            prepInsertActor.setInt(2, year);
            int affectedRows = prepInsertActor.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Vložení herce se nezdařilo.");
            }
            return 0;
        }

    }

    void addActorToMovie(String actorName, String movieName) {

        try {
            conn.setAutoCommit(false);

            prepQueryMoviesForName.setString(1, movieName);
            prepQueryActorsForName.setString(1, actorName);

            ResultSet movieId = prepQueryMoviesForName.executeQuery();
            ResultSet actorId = prepQueryActorsForName.executeQuery();

            if(!movieId.next() || !actorId.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chyba");
                alert.setHeaderText("Herec nebo film není v databázi.");

                alert.showAndWait();
            } else {
                prepInsertRelationship.setInt(1, actorId.getInt(1));
                prepInsertRelationship.setInt(2, movieId.getInt(1));
                int affectedRows = prepInsertRelationship.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Propojení herce a filmu se nezdařilo.");
                }
            }

            conn.setAutoCommit(true);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }

    }
