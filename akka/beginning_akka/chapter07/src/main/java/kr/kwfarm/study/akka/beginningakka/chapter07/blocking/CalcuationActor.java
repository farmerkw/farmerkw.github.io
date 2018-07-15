package kr.kwfarm.study.akka.beginningakka.chapter07.blocking;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.concurrent.TimeUnit;

public class CalcuationActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            Integer msg = (Integer)message;
            logger.info("CalculationActor received {}", msg);
            work(msg);
            getSender().tell(msg * 2, getSelf());
        }
    }

    private void work(Integer msg) throws InterruptedException {
        logger.info("CalculationActor working on " + msg);
        TimeUnit.SECONDS.sleep(1);
        logger.info("CalculationActor Completed " + msg);
    }
}
