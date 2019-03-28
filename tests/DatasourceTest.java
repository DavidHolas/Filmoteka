import controller.Datasource;
import org.junit.jupiter.api.*;
import model.Actor;
import model.Movie;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatasourceTest {

    @BeforeEach
    void setUp() {
        Datasource.getInstance().open();
    }


    @Test
    void queryMovies() {
        List<Movie> movies = Datasource.getInstance().queryMovies();
        assertEquals("Avatar", movies.get(0).getName());
    }

    @Test
    void getMoviesSize() {
        List<Movie> movies = Datasource.getInstance().queryMovies();
        assertEquals(28, movies.size());
    }

    @Test
    void returnedIdMovieAlreadyInserted() throws SQLException {
        int result = Datasource.getInstance().insertMovie("Jumanji", "Fantasy", 1995);
        assertEquals(2, result);
    }

    @org.junit.jupiter.api.Test
    void queryActors() {
        List<Actor> actors = Datasource.getInstance().queryActors();
        assertEquals("Al Pacino", actors.get(0).getName());
    }

    @Test
    void getActorSize() {
        List<Actor> actors = Datasource.getInstance().queryActors();
        assertEquals(26, actors.size());
    }

    @Test
    void returnedIdActorAlreadyInserted() throws SQLException {
        int result = Datasource.getInstance().insertActor("Tom Hanks", 1956);
        assertEquals(1, result);
    }

    @AfterEach
    void closeConection() {
        Datasource.getInstance().close();
    }
}