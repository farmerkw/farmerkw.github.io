package kr.kwfarm.study.akka.beginningakka.chapter05;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorWithStash;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class PingActor extends UntypedActorWithStash {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private ActorRef child;

    public PingActor() {
        child = context().actorOf(Props.create(Ping1Actor.class), "ping1Actor");
        getContext().become(initial);
    }

//    @Override
    public AbstractActor.Receive createReceive() {
        return null;
    }

    Procedure<Object> initial = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            logger.info("Ping1Actor Initial: {}", param);
            if ("work".equals(param)) {
                child.tell("work", getSelf());
                getContext().become(zeroDone);
            } else {
                stash();
            }
        }
    };

    Procedure<Object> zeroDone = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            logger.info("Ping1Actor zeroDone: {}", param);
            if ("done".equals(param)) {
                logger.info("Received the first done");
                getContext().become(oneDone);
            } else {
                stash();
            }
        }
    };

    Procedure<Object> oneDone = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            logger.info("Ping1Actor oneDone: {}", param);
            if ("done".equals(param)) {
                logger.info("Received the second done");
                unstashAll();
                getContext().become(allDone);
            } else {
                stash();
            }
        }
    };

    Procedure<Object> allDone = new Procedure<Object>() {
        @Override
        public void apply(Object param) throws Exception {
            logger.info("Ping1Actor allDone: {}", param);
            if ("reset".equals(param)) {
                logger.info("Received a reset");
                getContext().become(initial);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {

    }
}
