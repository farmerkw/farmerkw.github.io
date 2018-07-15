package kr.kwfarm.study.akka.beginningakka.chapter07.blocking;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.dispatch.OnComplete;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class NonblockingActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef child;

    private Timeout timeout = new Timeout(Duration.create(5, "second"));

    private final ExecutionContext ex;

    public NonblockingActor() {
        child = context().actorOf(Props.create(CalcuationActor.class), "calcurationActor");
        ex = context().system().dispatcher();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
       if (message instanceof Integer) {
           Future<Object> future = Patterns.ask(child, message, timeout);
            future.onSuccess(new SaySuccess<Object>(), ex);
            future.onFailure(new SayFailure<Object>(), ex);
            future.onComplete(new SayComplete<Object>(), ex);
       } else if (message instanceof String) {
           logger.info("NonBlockingActor Received a message: " + message);
       }
    }

    public final class SaySuccess<T> extends OnSuccess {
        @Override
        public void onSuccess(Object result) throws Throwable {
            logger.info("Successed with " + result);
        }
    }

    public final class SayFailure<T> extends OnFailure {
        @Override
        public void onFailure(Throwable failure) throws Throwable {
            logger.info("Failed with " + failure);
        }
    }

    public final class SayComplete<T> extends OnComplete {
        @Override
        public void onComplete(Throwable failure, Object success) throws Throwable {
            logger.info("Completed");
        }
    }
}
