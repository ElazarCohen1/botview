package fr.uge.database;

import fr.uge.git.GitProviderType;
import fr.uge.git.model.RepositoryDto;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class RepositoryQueries {
    private final DataSource dataSource;

    public  RepositoryQueries(DataSource dataSource){
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
        super();
    }

    @Transactional
    public RepositoryDto getRep(int repId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name, url, origin from REPOSITORY where id = ?");
            stmt.setInt(1, repId);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                return null;
            }
            return new RepositoryDto(res.getInt("id"), res.getString("name"), res.getString("url"), GitProviderType.valueOf(res.getString("origin")));
        }
    }

    @Transactional
    public void addRepo(String name, String url, GitProviderType provider) throws SQLException{
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO REPOSITORY (name,url,origin) VALUES ( ?, ?, ? )");
            stmt.setString(1, name);
            stmt.setString(2, url);
            stmt.setString(3, provider.name());
            stmt.execute();
        }
    }

    @Transactional
    public List<RepositoryDto> getAllRep(GitProviderType provider) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, name, url, origin from REPOSITORY where origin = ?");
            stmt.setString(1, provider.name());
            List<RepositoryDto> all_rep = new ArrayList<>();
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                all_rep.add(new RepositoryDto(res.getInt("id"), res.getString("name"), res.getString("url"), GitProviderType.valueOf(res.getString("origin"))));
            }
            return all_rep;
        }
    }

}
