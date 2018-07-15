package kr.kwfarm.study.akka.beginningakka.chapter03;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class PingActor extends UntypedAbstractActor {
    private LoggingAdapter logging = Logging.getLogger(getContext().system(), this);

    private ActorRef child;

    private int count = 0;

    public PingActor() {
        this.child = context().actorOf(Props.create(Ping1Actor.class), "ping1Actor");
    }
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String msg = (String)message;
            if ("work".equals(msg)) {
                child.tell(msg, getSelf());
            } else if("done".equals(msg)) {
                if (count == 0) {
                    count++;
                } else {
                    logging.info("all works are completed.");
                    count = 0;
                }
            }
        }

    }
}
