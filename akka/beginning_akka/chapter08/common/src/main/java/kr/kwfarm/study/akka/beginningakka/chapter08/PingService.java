package kr.kwfarm.study.akka.beginningakka.chapter08;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingService extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private int count = 0;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            logger.info("PingService Recive: {}", (String)message);
            String msg = (String)message;
            getSender().tell("PING: " + count++, getSelf());
        } else {
            unhandled(message);
        }
    }
}
