package kr.kwfarm.study.akka.beginningakka.chapter03;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Ping1Actor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef child1;

    private ActorRef child2;

    public Ping1Actor() {
        this.child1 = context().actorOf(Props.create(Ping2Actor.class));
        this.child2 = context().actorOf(Props.create(Ping3Actor.class));
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String msg = (String)message;
            logger.info("Ping1 Received {}", msg);
            child1.tell("work", getSender());
            child2.tell("work", getSender());
        }
    }
}
