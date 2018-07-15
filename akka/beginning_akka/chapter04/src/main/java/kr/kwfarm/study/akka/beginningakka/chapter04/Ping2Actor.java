package kr.kwfarm.study.akka.beginningakka.chapter04;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Optional;

public class Ping2Actor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public Ping2Actor() {
        logger.info("Ping2Actor Contructor");
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        logger.info("Ping2Actor preRestart");
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        logger.info("Ping2Actor postRestart");
    }

    @Override
    public void postStop() throws Exception {
        logger.info("Ping2Actor postStop");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            String msg = (String)message;

            if ("good".equals(msg)) {
                goodWork();
                getSender().tell("done", getSelf());
            } else if ("bad".equals(msg)) {
                badWork();
            }
        } else {
            unhandled(message);
        }

    }

    private void badWork() {
        int a = 1/0;
    }

    private void goodWork() {
        logger.info("Ping2Actor is Good");
    }
}
