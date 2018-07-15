package kr.kwfarm.study.akka.beginningakka.chapter04;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Optional;

public class Ping3Actor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public Ping3Actor() {
        logger.info("Ping3Actor Contructor");
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        logger.info("Ping3Actor preRestart");
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        logger.info("Ping3Actor postRestart");
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
        throw new NullPointerException();
    }

    private void goodWork() {
        logger.info("Ping3Actor is Good");
    }
}
