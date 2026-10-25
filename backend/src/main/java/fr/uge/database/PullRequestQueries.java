package fr.uge.database;

import fr.uge.model.PullRequest;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class PullRequestQueries {
    private final DataSource dataSource;
    public PullRequestQueries(DataSource dataSource){
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
        super();
    }

    @Transactional
    public List<PullRequest> getAllPr() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT pull_id, commentaire, name, date from PULL_REQUEST");
            List<PullRequest> all_rep = new ArrayList<>();
            while (res.next()) {
                all_rep.add(new PullRequest(res.getInt("pull_id"), res.getString("commentaire"), res.getString("name"), res.getTimestamp("date").toInstant()));
            }
            return all_rep;
        }
    }

    @Transactional
    public PullRequest getPr(int prId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT pull_id, commentaire, name, date from PULL_REQUEST where pull_id = ?");
            stmt.setInt(1, prId);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                return null;
            }
            return new PullRequest(res.getInt("pull_id"), res.getString("commentaire"), res.getString("name"), res.getTimestamp("date").toInstant());
        }
    }
}
