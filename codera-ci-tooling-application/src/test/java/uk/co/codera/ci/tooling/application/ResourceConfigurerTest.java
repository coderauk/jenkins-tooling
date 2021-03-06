package uk.co.codera.ci.tooling.application;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.co.codera.ci.tooling.application.CiToolingConfiguration.someCiToolingConfiguration;
import static uk.co.codera.ci.tooling.application.data.TestBitBucketConfigurations.randomBitBucketConfiguration;
import static uk.co.codera.ci.tooling.application.data.TestSvnConfigurations.randomSvnConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.dropwizard.jersey.setup.JerseyEnvironment;
import uk.co.codera.ci.tooling.api.bitbucket.BitBucketResource;
import uk.co.codera.ci.tooling.api.github.GitHubResource;
import uk.co.codera.ci.tooling.api.svn.SvnResource;
import uk.co.codera.ci.tooling.scm.ScmEventListener;

@RunWith(MockitoJUnitRunner.class)
public class ResourceConfigurerTest {

    @Mock
    private JerseyEnvironment mockJerseyEnvironment;

    @Mock
    private ScmEventListener mockScmEventListener;

    @Test
    public void shouldRegisterGitHubResource() {
        configure(someCiToolingConfiguration());
        verify(this.mockJerseyEnvironment).register(isA(GitHubResource.class));
    }

    @Test
    public void shouldNotRegisterBitBucketResourceIfNotConfigured() {
        configure(someCiToolingConfiguration());
        verify(this.mockJerseyEnvironment, never()).register(isA(BitBucketResource.class));
    }

    @Test
    public void shouldRegisterBitBucketResourceIfConfigured() {
        configure(someCiToolingConfiguration().with(randomBitBucketConfiguration()));
        verify(this.mockJerseyEnvironment).register(isA(BitBucketResource.class));
    }

    @Test
    public void shouldNotRegisterSvnResourceIfNotConfigured() {
        configure(someCiToolingConfiguration());
        verify(this.mockJerseyEnvironment, never()).register(isA(SvnResource.class));
    }

    @Test
    public void shouldRegisterSvnResourceIfConfigured() {
        configure(someCiToolingConfiguration().with(randomSvnConfiguration()));
        verify(this.mockJerseyEnvironment).register(isA(SvnResource.class));
    }

    private void configure(CiToolingConfiguration.Builder configuration) {
        ResourceConfigurer.configure(this.mockJerseyEnvironment, configuration.build(), this.mockScmEventListener);
    }
}