package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.HealthResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.service.client.common.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents health check client command service. */
@Service
public class HealthCheckClientCommandService implements IClientCommand<HealthCheckResult, Void> {
  @Autowired private ConfigService configService;

  private HealthResourceApi healthResourceApi;

  /**
   * @see IClientCommand
   */
  @Override
  @PostConstruct
  public void configure() {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.healthResourceApi = new HealthResourceApi(apiClient);
  }

  /**
   * @see IClientCommand
   */
  public HealthCheckResult process(Void input) throws ApiServerException {
    try {
      return healthResourceApi.v1HealthGet().block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
