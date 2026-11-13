package fr.uge;


import fr.uge.database.Database;
import fr.uge.git.GitProvider;
import fr.uge.git.GitProviderType;
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
    public void testAddRepository() throws SQLException{
        Assertions.assertThrows(NullPointerException.class, () -> database.getRepositoryQueries().addRepository(null,null,null));
        Assertions.assertThrows(NullPointerException.class, () -> database.getRepositoryQueries().addRepository("Name",null,null));
        Assertions.assertThrows(NullPointerException.class, () -> database.getRepositoryQueries().addRepository("Name","URL",null));
        database.getRepositoryQueries().addRepository("Name", "url", GitProviderType.BITBUCKET);
        var repository = database.getRepositoryQueries().getAllRepositories(GitProviderType.BITBUCKET);
        Assertions.assertEquals(1, repository.size());
        database.getRepositoryQueries().deleteRepository("Name", GitProviderType.BITBUCKET);
    }


    @Test
    public void testDeleteRepository() throws SQLException{
        Assertions.assertThrows(NullPointerException.class, () -> database.getRepositoryQueries().deleteRepository(null,null));
        Assertions.assertThrows(NullPointerException.class, () -> database.getRepositoryQueries().deleteRepository("Name",null));
        Assertions.assertThrows(NullPointerException.class, () -> database.getRepositoryQueries().deleteRepository(null,GitProviderType.GITLAB));

        database.getRepositoryQueries().addRepository("Name", "https://edt-consult.univ-eiffel.fr/portal/planning/calendar",GitProviderType.BITBUCKET);

        database.getRepositoryQueries().deleteRepository("Name", GitProviderType.GITLAB);
        var repository = database.getRepositoryQueries().getAllRepositories(GitProviderType.BITBUCKET);
        Assertions.assertEquals(1, repository.size());

        database.getRepositoryQueries().deleteRepository("Name", GitProviderType.BITBUCKET);
        repository = database.getRepositoryQueries().getAllRepositories(GitProviderType.BITBUCKET);
        Assertions.assertEquals(0, repository.size());
    }

    @Test
    public void testGetAllRepository() throws SQLException{
        database.getRepositoryQueries().addRepository("Name1", "url1", GitProviderType.BITBUCKET);
        database.getRepositoryQueries().addRepository("Name2", "url2", GitProviderType.BITBUCKET);
        database.getRepositoryQueries().addRepository("Name3", "url3", GitProviderType.BITBUCKET);
        database.getRepositoryQueries().addRepository("Name4", "url4", GitProviderType.BITBUCKET);
        var repository = database.getRepositoryQueries().getAllRepositories(GitProviderType.BITBUCKET);
        Assertions.assertEquals(4, repository.size());
        database.getRepositoryQueries().deleteRepository("Name1", GitProviderType.BITBUCKET);
        database.getRepositoryQueries().deleteRepository("Name2", GitProviderType.BITBUCKET);
        database.getRepositoryQueries().deleteRepository("Name3", GitProviderType.BITBUCKET);
        database.getRepositoryQueries().deleteRepository("Name4", GitProviderType.BITBUCKET);
        repository = database.getRepositoryQueries().getAllRepositories(GitProviderType.BITBUCKET);
        Assertions.assertEquals(0, repository.size());
    }

    @Test
    public void testAddSameUrl() throws SQLException{
        database.getRepositoryQueries().addRepository("BotView","https://github.com/botview", GitProviderType.GITLAB);
        Assertions.assertThrows(SQLException.class, () -> database.getRepositoryQueries().addRepository("BotView","https://github.com/botview", GitProviderType.GITLAB));
    }

    @Test
    public void testGetRepositoryId() throws SQLException{
        var repo = database.getRepositoryQueries().getRepository(11112026);
        Assertions.assertNull( repo );

        database.getRepositoryQueries().addRepository("BotView","https://github.com/botview", GitProviderType.GITLAB);
        var id = database.getRepositoryQueries().getAllRepositories(GitProviderType.GITLAB).getFirst().id();

        repo = database.getRepositoryQueries().getRepository(id);
        Assertions.assertEquals("BotView", repo.name());
        Assertions.assertEquals("https://github.com/botview", repo.url());
        Assertions.assertNotEquals(GitProviderType.BITBUCKET, repo.gitProviderType());
        Assertions.assertEquals(GitProviderType.GITLAB, repo.gitProviderType());

        database.getRepositoryQueries().deleteRepository("BotView", GitProviderType.GITLAB);


    }
}
