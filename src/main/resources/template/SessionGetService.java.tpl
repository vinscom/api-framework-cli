package in.erail.tutorial;

import com.google.common.net.MediaType;
import in.erail.model.Event;
import in.erail.service.RESTServiceImpl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.vertx.core.json.JsonArray;

public class SessionGetService extends RESTServiceImpl {

  private JsonArray mSessions = new JsonArray();

  public SessionGetService() {
    mSessions.add("S1");
    mSessions.add("S2");
    mSessions.add("S3");
    mSessions.add("S4");
    mSessions.add("S5");
  }

  @Override
  public MaybeSource<Event> process(Maybe<Event> pEvent) {
    return pEvent
            .doOnSuccess((e) -> {
              e.getResponse()
                      .setBody(getSessions().toString().getBytes())
                      .setMediaType(MediaType.JSON_UTF_8);
            });
  }

  public JsonArray getSessions() {
    return mSessions;
  }

  public void setSessions(JsonArray pSessions) {
    this.mSessions = pSessions;
  }

}
