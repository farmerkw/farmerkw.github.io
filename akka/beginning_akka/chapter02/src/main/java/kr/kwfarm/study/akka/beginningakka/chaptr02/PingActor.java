package kr.kwfarm.study.akka.beginningakka.chaptr02;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef pong;

    @Override
    public void preStart() throws Exception {
        this.pong = context().actorOf(Props.create(PongActor.class, getSelf()), "pongActor");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String msg = (String) message;
            logger.info("Ping Received {}", msg);
            pong.tell("ping", getSelf());
        }
    }
}
