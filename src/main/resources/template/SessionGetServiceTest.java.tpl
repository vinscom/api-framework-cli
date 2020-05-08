package [=package];

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import in.erail.glue.Glue;
import in.erail.server.Server;
import io.vertx.core.json.JsonArray;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.ext.web.client.WebClient;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
@Timeout(value = 10, timeUnit = TimeUnit.MINUTES)
public class SessionGetServiceTest {

  @Test
  public void testGetRequest(VertxTestContext testContext) {

    Server server = Glue.instance().<Server>resolve("/in/erail/server/Server");

    WebClient
            .create(server.getVertx())
            .get(server.getHttpServerOptions().getPort(), server.getHttpServerOptions().getHost(), "/v1/session")
            .putHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
            .rxSend()
            .doOnSuccess(response -> Assertions.assertEquals(200, response.statusCode()))
            .doOnSuccess((t) -> {
              JsonArray data = t.bodyAsJsonArray();
              Assertions.assertEquals(5, data.size());
            })
            .subscribe(t -> testContext.completeNow(), err -> testContext.failNow(err));
  }

}
