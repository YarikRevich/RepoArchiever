package com.repoachiever.service.vendor.git.github;

import com.repoachiever.dto.GitHubCommitAmountResponseDto;
import com.repoachiever.dto.GitHubDefaultBranchResponseDto;
import com.repoachiever.dto.GitHubLatestCommitHashResponseDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.GitHubContentIsEmptyException;
import com.repoachiever.exception.GitHubContentRetrievalFailureException;
import com.repoachiever.exception.GitHubGraphQlClientDocumentNotFoundException;
import com.repoachiever.exception.GitHubServiceNotAvailableException;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.communication.cluster.ClusterCommunicationConfigService;
import com.repoachiever.service.integration.scheduler.SchedulerConfigService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

/**
 * Service used to represent GitHub external service operations.
 */
@Service
public class GitGitHubVendorService {
    private static final Logger logger = LogManager.getLogger(GitGitHubVendorService.class);

    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ConfigService configService;

    @Autowired
    private VendorConfigurationHelper vendorConfigurationHelper;

    private HttpGraphQlClient graphQlClient;

    private WebClient restClient;

    private String document;

    /**
     * Performs initial GraphQL client and HTTP client configuration.
     *
     * @throws GitHubGraphQlClientDocumentNotFoundException if GitHub GraphQL client document is not found.
     */
    @PostConstruct
    private void configure() throws GitHubGraphQlClientDocumentNotFoundException {
        if (configService.getConfig().getService().getProvider() == ConfigEntity.Service.Provider.GIT_GITHUB) {
            WebClient client = WebClient.builder()
                    .baseUrl(properties.getGraphQlClientGitHubUrl())
                    .defaultHeader(
                            HttpHeaders.AUTHORIZATION,
                            vendorConfigurationHelper.getWrappedToken(
                                    configService.getConfig().getService().getCredentials().getToken()))
                    .build();

            this.graphQlClient = HttpGraphQlClient.builder(client).build();

            Resource resource = new ClassPathResource(properties.getGraphQlClientGitHubDocumentLocation());

            try {
                document = resource.getContentAsString(Charset.defaultCharset());
            } catch (IOException e) {
                throw new GitHubGraphQlClientDocumentNotFoundException(e.getMessage());
            }

            this.restClient = WebClient.builder()
                    .baseUrl(properties.getRestClientGitHubUrl())
                    .clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create().followRedirect(true)))
                    .defaultHeader(
                            HttpHeaders.AUTHORIZATION,
                            vendorConfigurationHelper.getWrappedToken(
                                    configService.getConfig().getService().getCredentials().getToken()))
                    .build();

//            this.restClient = RestClient.builder()
//                    .requestFactory(ClientHttpRequestFactories.get(new ClientHttpRequestFactorySettings(
//                            Duration.ofSeconds(properties.getRestClientTimeout()),
//                            Duration.ofSeconds(properties.getRestClientTimeout()),
//                            false)))
//                    .baseUrl(properties.getRestClientGitHubUrl())
//                    .defaultHeader(
//                            HttpHeaders.AUTHORIZATION,
//                            vendorConfigurationHelper.getWrappedToken(
//                                    configService.getConfig().getService().getCredentials().getToken()))
//                    .build();
        }
    }

    /**
     * Retrieves latest commit hash for the repository with the given name and given branch.
     *
     * @param owner  given repository owner.
     * @param name   given repository name.
     * @param branch given repository branch.
     * @return retrieved latest commit hash of the repository with the given name and given branch.
     * @throws GitHubContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     */
    public String getLatestCommitHash(String owner, String name, String branch) throws
            GitHubContentRetrievalFailureException {
        GitHubLatestCommitHashResponseDto gitHubLatestCommitHashResponse;

        try {
            gitHubLatestCommitHashResponse = graphQlClient
                    .document(document)
                    .variables(new HashMap<>() {
                        {
                            put("owner", owner);
                            put("name", name);
                            put("branch", branch);
                        }
                    })
                    .operationName("LatestCommitHash")
                    .retrieve("repository")
                    .toEntity(GitHubLatestCommitHashResponseDto.class)
                    .timeout(Duration.ofSeconds(properties.getGraphQlClientTimeout()))
                    .onErrorResume(element -> Mono.empty())
                    .block();

        } catch (WebClientResponseException e) {
            throw new GitHubContentRetrievalFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new GitHubContentRetrievalFailureException(
                    new GitHubServiceNotAvailableException(e.getMessage()).getMessage());
        }

        if (Objects.isNull(gitHubLatestCommitHashResponse)) {
            throw new GitHubContentRetrievalFailureException();
        }

        return gitHubLatestCommitHashResponse
                .getRef()
                .getTarget()
                .getHistory()
                .getNodes()
                .getFirst()
                .getOid();
    }

    /**
     * Retrieves default branch for the repository with the given name.
     *
     * @param owner given repository owner.
     * @param name  given repository name.
     * @return retrieved default branch of the repository with the given name.
     * @throws GitHubContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     */
    public String getDefaultBranch(String owner, String name) throws
            GitHubContentRetrievalFailureException {
        GitHubDefaultBranchResponseDto gitHubDefaultBranchResponse;

        try {
            gitHubDefaultBranchResponse = graphQlClient
                    .document(document)
                    .variables(new HashMap<>() {
                        {
                            put("owner", owner);
                            put("name", name);
                        }
                    })
                    .operationName("DefaultBranch")
                    .retrieve("repository")
                    .toEntity(GitHubDefaultBranchResponseDto.class)
                    .timeout(Duration.ofSeconds(properties.getGraphQlClientTimeout()))
                    .onErrorResume(element -> Mono.empty())
                    .block();

        } catch (WebClientResponseException e) {
            throw new GitHubContentRetrievalFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new GitHubContentRetrievalFailureException(
                    new GitHubServiceNotAvailableException(e.getMessage()).getMessage());
        }

        if (Objects.isNull(gitHubDefaultBranchResponse)) {
            throw new GitHubContentRetrievalFailureException();
        }

        return gitHubDefaultBranchResponse
                .getDefaultBranchRef()
                .getName();
    }

    /**
     * Retrieves amount of commits for the repository with the given name and given branch.
     *
     * @param owner  given repository owner.
     * @param name   given repository name.
     * @param branch given repository branch.
     * @return retrieved amount of repository commits with the given name and given branch.
     * @throws GitHubContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     */
    public Integer getCommitAmount(String owner, String name, String branch) throws
            GitHubContentRetrievalFailureException {
        GitHubCommitAmountResponseDto gitHubCommitAmountResponse;

        try {
            gitHubCommitAmountResponse = graphQlClient
                    .document(document)
                    .variables(new HashMap<>() {
                        {
                            put("owner", owner);
                            put("name", name);
                            put("branch", branch);
                        }
                    })
                    .operationName("CommitAmount")
                    .retrieve("repository")
                    .toEntity(GitHubCommitAmountResponseDto.class)
                    .timeout(Duration.ofSeconds(properties.getGraphQlClientTimeout()))
                    .onErrorResume(element -> Mono.empty())
                    .block();

        } catch (WebClientResponseException e) {
            throw new GitHubContentRetrievalFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new GitHubContentRetrievalFailureException(
                    new GitHubServiceNotAvailableException(e.getMessage()).getMessage());
        }

        if (Objects.isNull(gitHubCommitAmountResponse)) {
            throw new GitHubContentRetrievalFailureException();
        }

        return gitHubCommitAmountResponse
                .getRef()
                .getTarget()
                .getHistory()
                .getTotalCount();
    }

    /**
     * Retrieves content from the repository with the given name and given commit hash.
     *
     * @param owner      given repository owner.
     * @param name       given repository name.
     * @param commitHash given commit hash.
     * @param format     given content format type.
     * @return retrieved content from the repository with the given name and given commit hash as an input stream.
     * @throws GitHubContentRetrievalFailureException if GitHub REST API client content retrieval fails.
     */
    public InputStream getCommitContent(String owner, String name, String format, String commitHash) throws
            GitHubContentRetrievalFailureException {
        DataBuffer resource = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(
                                String.format("/repos/%s/%s/%s/%s", owner, name, format, commitHash))
                        .build())
                .retrieve()
                .bodyToMono(DataBuffer.class)
                .timeout(Duration.ofSeconds(properties.getRestClientTimeout()))
                .onErrorResume(element -> Mono.empty())
                .block();

        if (Objects.isNull(resource)) {
            throw new GitHubContentRetrievalFailureException(new GitHubContentIsEmptyException().getMessage());
        }

        return resource.asInputStream();
    }

    /**
     * Retrieves content from the repository with the given name and given commit hash in zip format.
     *
     * @param owner      given repository owner.
     * @param name       given repository name.
     * @param commitHash given commit hash.
     * @return retrieved content from the repository with the given name and given commit hash as an input stream.
     * @throws GitHubContentRetrievalFailureException if GitHub REST API client content retrieval fails.
     */
    public InputStream getCommitContentAsZip(String owner, String name, String commitHash) throws
            GitHubContentRetrievalFailureException {
        return getCommitContent(owner, name, "zipball", commitHash);
    }

    /**
     * Retrieves content from the repository with the given name and given commit hash in tar format.
     *
     * @param owner      given repository owner.
     * @param name       given repository name.
     * @param commitHash given commit hash.
     * @return retrieved content from the repository with the given name and given commit hash as an input stream.
     * @throws GitHubContentRetrievalFailureException if GitHub REST API client content retrieval fails.
     */
    public InputStream getCommitContentAsTar(String owner, String name, String commitHash) throws
            GitHubContentRetrievalFailureException {
        return getCommitContent(owner, name, "tarball", commitHash);
    }
}
