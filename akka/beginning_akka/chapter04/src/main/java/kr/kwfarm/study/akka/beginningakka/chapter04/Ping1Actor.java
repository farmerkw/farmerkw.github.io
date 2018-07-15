package kr.kwfarm.study.akka.beginningakka.chapter04;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

public class Ping1Actor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private static SupervisorStrategy strategy = new OneForOneStrategy(10, Duration.create("1 minute"),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
                    if (param instanceof ArithmeticException) {
                        return SupervisorStrategy.resume();
                    } else if (param instanceof NullPointerException) {
                        return SupervisorStrategy.restart();
                    } else if (param instanceof IllegalArgumentException) {
                        return SupervisorStrategy.stop();
                    } else {
                        return SupervisorStrategy.escalate();
                    }
                }
            });

    private ActorRef child01;

    private ActorRef child02;

    public Ping1Actor() {
        child01 = context().actorOf(Props.create(Ping2Actor.class), "ping2Actor");
        child02 = context().actorOf(Props.create(Ping3Actor.class), "ping3Actor");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
           String msg = (String)message;
           if ("good".equals(msg) || "bad".equals(msg)) {
               logger.info("Ping1Actor received {}", msg);
               child01.tell(message, getSender());
               child02.tell(message, getSender());
           }
        } else {
            unhandled(message);
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}
