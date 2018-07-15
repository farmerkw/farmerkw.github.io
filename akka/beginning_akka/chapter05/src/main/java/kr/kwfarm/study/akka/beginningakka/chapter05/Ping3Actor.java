package kr.kwfarm.study.akka.beginningakka.chapter05;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.concurrent.TimeUnit;

public class Ping3Actor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
       if (message instanceof String) {
           String msg = (String) message;
           logger.info("Ping3Actor Received {}", msg);
           work();
           getSender().tell("done", getSelf());
       }
    }

    private void work() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        logger.info("Ping3 Working");
    }
}
