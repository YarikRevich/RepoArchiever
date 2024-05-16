package com.repoachiever.service.vendor.git.github;

import com.repoachiever.dto.GitHubCommitAmountResponseDto;
import com.repoachiever.dto.GitHubDefaultBranchResponseDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.GitHubGraphQlClientContentRetrievalFailureException;
import com.repoachiever.exception.GitHubGraphQlClientDocumentNotFoundException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

/**
 * Service used to represent GitHub external service operations.
 */
@Service
public class GitGitHubVendorService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ConfigService configService;

    @Autowired
    private VendorConfigurationHelper vendorConfigurationHelper;

    private HttpGraphQlClient graphQlClient;

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
        }
    }

    /**
     * Retrieves default branch for the repository with the given name.
     *
     * @param owner  given repository owner.
     * @param name   given repository name.
     * @return retrieved default branch of the repository with the given name.
     * @throws GitHubGraphQlClientContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     */
    public String getDefaultBranch(String owner, String name) throws
            GitHubGraphQlClientContentRetrievalFailureException {
        GitHubDefaultBranchResponseDto gitHubCommitAmountResponse;

        try {
            gitHubCommitAmountResponse = graphQlClient
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
                    .block();

        } catch (WebClientResponseException e) {
            throw new GitHubGraphQlClientContentRetrievalFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new GitHubGraphQlClientContentRetrievalFailureException(e.getMessage());
        }

        if (Objects.isNull(gitHubCommitAmountResponse)) {
            throw new GitHubGraphQlClientContentRetrievalFailureException();
        }

        return gitHubCommitAmountResponse
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
     * @throws GitHubGraphQlClientContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     */
    public Integer getCommitAmount(String owner, String name, String branch) throws
            GitHubGraphQlClientContentRetrievalFailureException {
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
                    .block();

        } catch (WebClientResponseException e) {
            throw new GitHubGraphQlClientContentRetrievalFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new GitHubGraphQlClientContentRetrievalFailureException(e.getMessage());
        }

        if (Objects.isNull(gitHubCommitAmountResponse)) {
            throw new GitHubGraphQlClientContentRetrievalFailureException();
        }

        return gitHubCommitAmountResponse
                .getRef()
                .getTarget()
                .getHistory()
                .getTotalCount();
    }
}
