package kr.kwfarm.study.akka.beginningakka.chapter07.blocking;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class BlockingActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef child;

    private Timeout timeout = new Timeout(Duration.create(5, "second"));

    private final ExecutionContext ec;

    public BlockingActor() {
        this.child = context().actorOf(Props.create(CalcuationActor.class), "calcuationActor");
        this.ec = context().system().dispatcher();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            Future<Object> future = Patterns.ask(child, message, timeout);
            Integer result = (Integer)Await.result(future, timeout.duration());
            logger.info("Final result is " + (result + 1));
        } else if (message instanceof String) {
            logger.info("BlockingActor Received a message: " + message);
        }
    }
}
