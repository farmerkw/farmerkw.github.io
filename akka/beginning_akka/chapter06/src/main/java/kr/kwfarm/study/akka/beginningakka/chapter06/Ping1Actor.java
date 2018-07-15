package kr.kwfarm.study.akka.beginningakka.chapter06;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.concurrent.TimeUnit;

public class Ping1Actor  extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            Integer msg = (Integer)message;
            logger.info("Ping1Actor({}) recevied {}", hashCode(), msg);
            work(msg);
        }
    }

    private void work(Integer msg) throws InterruptedException {
        logger.info("Ping1Actor({}) working on {}", hashCode(), msg);
        TimeUnit.SECONDS.sleep(1);
        logger.info("Ping1Actor({}) completed", hashCode());
    }
}
