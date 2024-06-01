package com.repoachiever.service.telemetry.binding;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service used to create custom telemetry bindings used to distribute application metrics.
 */
@ApplicationScoped
public class TelemetryBinding implements MeterBinder {
    @Getter
    private final AtomicInteger workerAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger servingClusterAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger suspendedClusterAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger apiServerHealthCheckAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger clusterHealthCheckAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger rawContentUploadAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger additionalContentUploadAmount = new AtomicInteger();

    /**
     * @see MeterBinder
     */
    @Override
    public void bindTo(@NotNull MeterRegistry meterRegistry) {
        Gauge.builder("general.worker_amount", workerAmount, AtomicInteger::get)
                .description("Represents amount of allocated workers")
                .register(meterRegistry);

        Gauge.builder("general.serving_cluster_amount", servingClusterAmount, AtomicInteger::get)
                .description("Represents amount of serving RepoAchiever Cluster allocations")
                .register(meterRegistry);

        Gauge.builder("general.suspended_cluster_amount", suspendedClusterAmount, AtomicInteger::get)
                .description("Represents amount of suspended RepoAchiever Cluster allocations")
                .register(meterRegistry);

        Gauge.builder("general.api_server_health_check_amount", apiServerHealthCheckAmount, AtomicInteger::get)
                .description("Represents amount of performed health check requests for RepoAchiever API Server instance")
                .register(meterRegistry);

        Gauge.builder("general.cluster_health_check_amount", clusterHealthCheckAmount, AtomicInteger::get)
                .description("Represents amount of performed health check requests for RepoAchiever Cluster allocations")
                .register(meterRegistry);

        Gauge.builder("general.raw_content_upload_amount", rawContentUploadAmount, AtomicInteger::get)
                .description("Represents amount of performed raw content upload requests for RepoAchiever Cluster allocations")
                .register(meterRegistry);

        Gauge.builder("general.additional_content_upload_amount", additionalContentUploadAmount, AtomicInteger::get)
                .description("Represents amount of performed additional content upload requests for RepoAchiever Cluster allocations")
                .register(meterRegistry);
    }
}
