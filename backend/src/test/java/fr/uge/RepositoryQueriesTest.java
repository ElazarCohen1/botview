package fr.uge;


import fr.uge.database.Database;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@MicronautTest
public class RepositoryQueriesTest {

  @Inject
  Database database;

  @Test
  void testAddRepository() throws SQLException {
    Assertions.assertThrows(IllegalArgumentException.class,()-> database.getRepositoryQueries().getAllRepositories(null));
//    database.getRepositoryQueries().addRepository();
  }


}
