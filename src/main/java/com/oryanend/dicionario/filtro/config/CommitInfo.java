package com.oryanend.dicionario.filtro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true)
public class CommitInfo {
  @Value("${git.commit.message.full:}")
  private String commitMessage;

  @Value("${git.commit.user.name:}")
  private String commitAuthor;

  @Value("${git.commit.time:}")
  private String commitTime;

  @Value("${git.commit.id.abbrev:}")
  private String commitId;

  public String getCommitMessage() {
    return commitMessage.isBlank() ? null : commitMessage;
  }

  public String getCommitAuthor() {
    return commitAuthor.isBlank() ? null : commitAuthor;
  }

  public String getCommitTime() {
    return commitTime.isBlank() ? null : commitTime;
  }

  public String getCommitId() {
    return commitId.isBlank() ? null : commitId;
  }

  public boolean isAvailable() {
    return getCommitId() != null;
  }
}
