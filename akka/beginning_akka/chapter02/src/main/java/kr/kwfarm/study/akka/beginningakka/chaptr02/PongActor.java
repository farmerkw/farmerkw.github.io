package kr.kwfarm.study.akka.beginningakka.chaptr02;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.concurrent.TimeUnit;

public class PongActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef ping;

    public PongActor(ActorRef ping) {
        this.ping = ping;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String msg = (String)message;
            logger.info("Pong Received {}", msg);
            ping.tell("pong", getSelf());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
