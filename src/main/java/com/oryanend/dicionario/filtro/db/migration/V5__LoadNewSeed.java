package com.oryanend.dicionario.filtro.db.migration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.extensibility.MigrationType;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.springframework.stereotype.Component;

@SuppressWarnings("checkstyle:TypeName")
@Component
public class V5__LoadNewSeed extends BaseJavaMigration {
  private static final String FILE_PATH = "/db/dic.txt";
  private static final String INSERT_SQL = "INSERT INTO words(word) VALUES (?)";

  @Override
  public ResolvedMigration getResolvedMigration(
      Configuration config, StatementInterceptor statementInterceptor) {
    return super.getResolvedMigration(config, statementInterceptor);
  }

  @Override
  public void migrate(Context context) throws Exception {
    Connection connection = context.getConnection();

    connection.setAutoCommit(false);

    InputStream inputStream = getClass().getResourceAsStream(FILE_PATH);
    if (inputStream == null) {
      throw new FileNotFoundException(
          "Arquivo de importação não encontrado no classpath: " + FILE_PATH);
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
        int batchSize = 0;
        final int MAX_BATCH_SIZE = 1000;

        while ((line = reader.readLine()) != null) {
          String word = line.trim();
          if (!word.isEmpty()) {
            statement.setString(1, word);
            statement.addBatch();
            batchSize++;

            if (batchSize >= MAX_BATCH_SIZE) {
              statement.executeBatch();
              statement.clearBatch();
              batchSize = 0;
            }
          }
        }

        if (batchSize > 0) {
          statement.executeBatch();
        }
      }
      connection.commit();
    } catch (SQLException e) {
      connection.rollback();
      throw e;
    }
  }

  @Override
  public MigrationType getType() {
    return super.getType();
  }
}
