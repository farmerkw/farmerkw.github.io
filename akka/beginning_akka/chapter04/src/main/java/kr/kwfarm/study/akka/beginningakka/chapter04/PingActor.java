package kr.kwfarm.study.akka.beginningakka.chapter04;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef child;

    public PingActor() {
        child = context().actorOf(Props.create(Ping1Actor.class), "ping1Actor");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
           String msg = (String) message;

           if ("good".equals(msg) || "bad".equals(msg)) {
               child.tell(msg, getSelf());
           } else if ("done".equals(msg)) {
               logger.info("all works are successully completed");
           } else {
               logger.info("Not Defind Message: {}", msg);
           }
        } else {
            logger.info("PingActor unhandled");
            unhandled(message);
        }

    }
}
