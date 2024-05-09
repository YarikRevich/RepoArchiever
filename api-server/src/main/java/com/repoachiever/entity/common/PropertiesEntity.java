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

    @ConfigProperty(name = "database.tables.config.name")
    String databaseConfigTableName;

    @ConfigProperty(name = "database.tables.content.name")
    String databaseContentTableName;

    @ConfigProperty(name = "database.tables.provider.name")
    String databaseProviderTableName;

    @ConfigProperty(name = "database.tables.secret.name")
    String databaseSecretTableName;

    @ConfigProperty(name = "database.statement.close-delay")
    Integer databaseStatementCloseDelay;

    @ConfigProperty(name = "bin.directory")
    String binDirectory;

    @ConfigProperty(name = "bin.cluster.location")
    String binClusterLocation;

    @ConfigProperty(name = "config.directory")
    String configDirectory;

    @ConfigProperty(name = "config.name")
    String configName;

    @ConfigProperty(name = "workspace.directory")
    String workspaceDirectory;

    @ConfigProperty(name = "workspace.content.directory")
    String workspaceContentDirectory;

    @ConfigProperty(name = "workspace.content.version-amount")
    Integer workspaceContentVersionAmount;

    @ConfigProperty(name = "workspace.metadata.directory")
    String workspaceMetadataDirectory;

    @ConfigProperty(name = "workspace.prs-metadata-file.name")
    String workspacePRsMetadataFileName;

    @ConfigProperty(name = "workspace.issues-metadata-file.name")
    String workspaceIssuesMetadataFileName;

    @ConfigProperty(name = "workspace.releases-metadata-file.name")
    String workspaceReleasesMetadataFileName;

    @ConfigProperty(name = "repoachiever-cluster.context.alias")
    String clusterContextAlias;

    @ConfigProperty(name = "communication.provider.name")
    String communicationProviderName;

    @ConfigProperty(name = "communication.cluster.base")
    String communicationClusterBase;

    @ConfigProperty(name = "communication.cluster.startup-await-frequency")
    Integer communicationClusterStartupAwaitFrequency;

    @ConfigProperty(name = "communication.cluster.startup-timeout")
    Integer communicationClusterStartupTimeout;

    @ConfigProperty(name = "communication.cluster.health-check-frequency")
    Integer communicationClusterHealthCheckFrequency;

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
