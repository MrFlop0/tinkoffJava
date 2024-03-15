package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationTest {
    private static final PostgreSQLContainer<?> POSTGRES = IntegrationTest.POSTGRES;

    @Test
    public void testMigrations() throws SQLException {
        String insert = "INSERT INTO link (link, type, update_date, previous_check) VALUES ('test', 1, now(), now())";
        String select = "SELECT * FROM link";

        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );

        int count = connection.createStatement().executeUpdate(insert);
        ResultSet result = connection.createStatement().executeQuery(select);

        assertThat(count).isEqualTo(1);
        assertThat(result.next()).isTrue();
        assertThat(result.getString("link")).isEqualTo("test");
        assertThat(result.getInt("type")).isEqualTo(1);

        String delete = "DELETE FROM link";
        connection.createStatement().executeUpdate(delete);
        connection.close();
    }

}
