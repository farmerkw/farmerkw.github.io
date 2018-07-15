package kr.kwfarm.study.akka.beginningakka.chapter07.agent;

import akka.actor.UntypedAbstractActor;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Mapper;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.ExecutionContext;

import java.util.concurrent.TimeUnit;

public class AgentActor extends UntypedAbstractActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        ExecutionContext ec = ExecutionContexts.global();
        Agent<Integer> agent = Agent.create(5, ec);

        agent.send(new Mapper<Integer, Integer>() {
            public Integer apply(Integer i) {
                return i * 2;
            }
        });

        logger.info("Current Agent Value: {}", agent.get());
        TimeUnit.SECONDS.sleep(5);
        logger.info("Current Agent Value: {}", agent.get());
    }
}
