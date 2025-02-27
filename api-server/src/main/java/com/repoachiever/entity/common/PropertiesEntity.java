package com.repoachiever.entity.common;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Exposes access to properties setup to be used for further configuration.
 */
@Getter
@ApplicationScoped
public class PropertiesEntity {
    @ConfigProperty(name = "quarkus.application.version")
    String applicationVersion;

    @ConfigProperty(name = "quarkus.http.host")
    String applicationHost;

    @ConfigProperty(name = "quarkus.http.port")
    Integer applicationPort;

    @ConfigProperty(name = "quarkus.rest-client.github.url")
    String restClientGitHubUrl;

    @ConfigProperty(name = "rest-client.github.page-size")
    Integer restClientGitHubPageSize;

    @ConfigProperty(name = "rest-client.github.max-page")
    Integer restClientGitHubMaxPage;

    @ConfigProperty(name = "rest-client.github.repo-visibility")
    String restClientGitHubRepoVisibility;

    @ConfigProperty(name = "github.location.notation")
    String gitHubLocationNotation;

    @ConfigProperty(name = "state.location")
    String stateLocation;

    @ConfigProperty(name = "state.running.name")
    String stateRunningName;

    @ConfigProperty(name = "database.tables.config.name")
    String databaseConfigTableName;

    @ConfigProperty(name = "database.tables.content.name")
    String databaseContentTableName;

    @ConfigProperty(name = "database.tables.provider.name")
    String databaseProviderTableName;

    @ConfigProperty(name = "database.tables.exporter.name")
    String databaseExporterTableName;

    @ConfigProperty(name = "database.tables.secret.name")
    String databaseSecretTableName;

    @ConfigProperty(name = "database.statement.close-delay")
    Integer databaseStatementCloseDelay;

    @ConfigProperty(name = "cluster.bin.location")
    String clusterBinLocation;

    @ConfigProperty(name = "config.location")
    String configLocation;

    @ConfigProperty(name = "workspace.directory")
    String workspaceDirectory;

    @ConfigProperty(name = "workspace.content.raw.directory")
    String workspaceRawContentDirectory;

    @ConfigProperty(name = "workspace.content.additional.directory")
    String workspaceAdditionalContentDirectory;

    @ConfigProperty(name = "repoachiever-cluster.context.alias")
    String clusterContextAlias;

    @ConfigProperty(name = "communication.api-server.name")
    String communicationApiServerName;

    @ConfigProperty(name = "communication.cluster.base")
    String communicationClusterBase;

    @ConfigProperty(name = "communication.cluster.startup-await-frequency")
    Integer communicationClusterStartupAwaitFrequency;

    @ConfigProperty(name = "communication.cluster.startup-timeout")
    Integer communicationClusterStartupTimeout;

    @ConfigProperty(name = "communication.cluster.health-check.frequency")
    Integer communicationClusterHealthCheckFrequency;

    @ConfigProperty(name = "diagnostics.scrape.delay")
    Integer diagnosticsScrapeDelay;

    @ConfigProperty(name = "diagnostics.common.docker.network.name")
    String diagnosticsCommonDockerNetworkName;

    @ConfigProperty(name = "diagnostics.grafana.config.location")
    String diagnosticsGrafanaConfigLocation;

    @ConfigProperty(name = "diagnostics.grafana.datasources.location")
    String diagnosticsGrafanaDatasourcesLocation;

    @ConfigProperty(name = "diagnostics.grafana.datasources.template")
    String diagnosticsGrafanaDatasourcesTemplate;

    @ConfigProperty(name = "diagnostics.grafana.datasources.output")
    String diagnosticsGrafanaDatasourcesOutput;

    @ConfigProperty(name = "diagnostics.grafana.dashboards.location")
    String diagnosticsGrafanaDashboardsLocation;

    @ConfigProperty(name = "diagnostics.grafana.dashboards.diagnostics.template")
    String diagnosticsGrafanaDashboardsDiagnosticsTemplate;

    @ConfigProperty(name = "diagnostics.grafana.dashboards.diagnostics.output")
    String diagnosticsGrafanaDashboardsDiagnosticsOutput;

    @ConfigProperty(name = "diagnostics.grafana.internal.location")
    String diagnosticsGrafanaInternalLocation;

    @ConfigProperty(name = "diagnostics.grafana.docker.name")
    String diagnosticsGrafanaDockerName;

    @ConfigProperty(name = "diagnostics.grafana.docker.image")
    String diagnosticsGrafanaDockerImage;

    @ConfigProperty(name = "diagnostics.prometheus.config.location")
    String diagnosticsPrometheusConfigLocation;

    @ConfigProperty(name = "diagnostics.prometheus.config.template")
    String diagnosticsPrometheusConfigTemplate;

    @ConfigProperty(name = "diagnostics.prometheus.config.output")
    String diagnosticsPrometheusConfigOutput;

    @ConfigProperty(name = "diagnostics.prometheus.internal.location")
    String diagnosticsPrometheusInternalLocation;

    @ConfigProperty(name = "diagnostics.prometheus.docker.name")
    String diagnosticsPrometheusDockerName;

    @ConfigProperty(name = "diagnostics.prometheus.docker.image")
    String diagnosticsPrometheusDockerImage;

    @ConfigProperty(name = "diagnostics.prometheus.node-exporter.docker.name")
    String diagnosticsPrometheusNodeExporterDockerName;

    @ConfigProperty(name = "diagnostics.prometheus.node-exporter.docker.image")
    String diagnosticsPrometheusNodeExporterDockerImage;

    @ConfigProperty(name = "diagnostics.metrics.connection.timeout")
    Integer diagnosticsMetricsConnectionTimeout;

    @ConfigProperty(name = "git.commit.id.abbrev")
    String gitCommitId;

    /**
     * Removes the last symbol in git commit id of the repository.
     *
     * @return chopped repository git commit id.
     */
    public String getGitCommitId() {
        return StringUtils.chop(gitCommitId);
    }
}
