package proj.concert.service.jaxrs;

import javax.ws.rs.container.AsyncResponse;

public class ConcertSubscription {
    public final AsyncResponse response;
    public final double targetPercentage;
    public ConcertSubscription(AsyncResponse response, double targetPercentage) {
        this.response = response;
        this.targetPercentage = targetPercentage;
    }
}
