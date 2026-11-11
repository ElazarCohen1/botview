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

    public PullRequestQueries(DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
        super();
    }

    /**
     * Gets eveery pull requests from the database
     *
     * @return a list of all stored pull requests in datadbase
     * @throws SQLException if there is database error
     */
    public List<PullRequest> getAllPullRequest() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT id, name, created_at from PULL_REQUEST");
            List<PullRequest> pullRequests = new ArrayList<>();
            while (res.next()) {
                pullRequests.add(new PullRequest(res.getInt("id"), res.getString("name"),
                        res.getTimestamp("created_at").toInstant()));
            }
            return pullRequests;
        }
    }

    /**
     * Get a specific pull request from the database
     *
     * @param prId id of the pull request by its identifier
     * @return the pull request
     * @throws SQLException if there is database error
     */
    public PullRequest getPullRequest(int prId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT id, name, created_at from PULL_REQUEST where id = ?");
            stmt.setInt(1, prId);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                return null;
            }
            return new PullRequest(res.getInt("id"), res.getString("name"),
                    res.getTimestamp("created_at").toInstant());
        }
    }
}
