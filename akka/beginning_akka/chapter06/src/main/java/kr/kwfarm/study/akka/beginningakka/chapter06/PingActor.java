package kr.kwfarm.study.akka.beginningakka.chapter06;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;

public class PingActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef childRounter;

    public PingActor() {
        childRounter = getContext()
                .actorOf(new RoundRobinPool(5).props(Props.create(Ping1Actor.class)), "ping1Actor");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            for (int i = 0; i < 10; i++) {
                childRounter.tell(i, getSelf());
            }
            logger.info("PingActor sent 10 message to router");
        } else {
            unhandled(message);
        }
    }
}
