package uk.co.codera.ci.tooling.api.bitbucket;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.codera.ci.tooling.api.bitbucket.dto.PushEventDto;
import uk.co.codera.ci.tooling.git.GitEventListener;
import uk.co.codera.ci.tooling.git.GitPushEvent;

@Path("/bitbucket")
@Consumes(MediaType.APPLICATION_JSON)
public class BitBucketResource {

    @SuppressWarnings("squid:S1312")
    private final Logger logger;
    private final PushEventDtoAdapter gitPushEventAdapter;
    private final GitEventListener gitEventListener;

    public BitBucketResource(Logger logger, PushEventDtoAdapter gitPushEventAdapter, GitEventListener gitEventListener) {
        this.logger = logger;
        this.gitPushEventAdapter = gitPushEventAdapter;
        this.gitEventListener = gitEventListener;
    }

    public BitBucketResource(PushEventDtoAdapter gitPushEventAdapter, GitEventListener gitEventListener) {
        this(LoggerFactory.getLogger(BitBucketResource.class), gitPushEventAdapter, gitEventListener);
    }

    @POST
    public void push(PushEventDto pushEvent) {
        this.logger.info("Received push event [{}]", pushEvent);
        GitPushEvent gitPushEvent = this.gitPushEventAdapter.from(pushEvent);

        if (gitPushEvent.getReference().isTag()) {
            this.logger.info("Ignoring event because it is not related to a branch");
        } else {
            this.gitEventListener.on(gitPushEvent);
        }
    }
}