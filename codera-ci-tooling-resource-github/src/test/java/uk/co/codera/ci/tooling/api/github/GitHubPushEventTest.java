package uk.co.codera.ci.tooling.api.github;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static uk.co.codera.ci.tooling.api.github.GitHubPushEvents.aValidPushEvent;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.codera.ci.tooling.api.github.GitHubPushEvent;
import uk.co.codera.ci.tooling.api.github.Repository;
import uk.co.codera.lang.io.ClasspathResource;

public class GitHubPushEventTest {

    private static final String PATH_JSON_BRANCH_CREATED = "/git/branch-created.json";

    private ObjectMapper objectMapper;

    @Before
    public void before() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldBeAbleToReadValidPushEvent() {
        assertThat(readPushEvent(), is(notNullValue()));
    }

    @Test
    public void toStringShouldNotBeObjectReference() {
        assertThat(readPushEvent().toString(), containsString("sshUrl="));
    }

    @Test
    public void pushEventShouldHaveRef() {
        assertThat(readPushEvent().getRef(), is(notNullValue()));
    }

    @Test
    public void pushEventShouldHaveRefType() {
        assertThat(readPushEvent().getRefType(), is(notNullValue()));
    }

    @Test
    public void pushEventShouldHaveRepositoryInformation() {
        assertThat(readPushEvent().getRepository(), is(notNullValue()));
    }

    @Test
    public void repositoryShouldHaveName() {
        assertThat(repository().getName(), is(notNullValue()));
    }

    @Test
    public void repositoryShouldHaveSshUrl() {
        assertThat(repository().getSshUrl(), is(notNullValue()));
    }

    @Test
    public void shouldReportRefTypeBranchIsBranch() {
        assertThat(aValidPushEvent().refType(GitHubPushEvent.REF_TYPE_BRANCH).build().isBranch(), is(true));
    }

    @Test
    public void shouldReportRefTypeOtherThanBranchIsNotBranch() {
        assertThat(aValidPushEvent().refType(GitHubPushEvent.REF_TYPE_TAG).build().isBranch(), is(false));
    }

    @Test
    public void shouldReportNormalBranchNameAsNotGitHubPagesBranch() {
        assertThat(aValidPushEvent().ref("my-branch").build().isGitHubPages(), is(false));
    }

    @Test
    public void shouldReportGitHubPagesBranchNameAsGitHubPagesBranch() {
        assertThat(aValidPushEvent().ref("gh-pages").build().isGitHubPages(), is(true));
    }

    private Repository repository() {
        return readPushEvent().getRepository();
    }

    private GitHubPushEvent readPushEvent() {
        return readPushEvent(jsonForBranchCreated());
    }

    private GitHubPushEvent readPushEvent(String json) {
        return read(json, GitHubPushEvent.class);
    }

    private <T> T read(String json, Class<T> targetClass) {
        try {
            return this.objectMapper.readValue(json, targetClass);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String jsonForBranchCreated() {
        return json(PATH_JSON_BRANCH_CREATED);
    }

    private String json(String path) {
        return new ClasspathResource(path).getAsString();
    }
}