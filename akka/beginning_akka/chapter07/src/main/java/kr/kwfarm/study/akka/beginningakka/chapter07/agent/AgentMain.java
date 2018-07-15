package kr.kwfarm.study.akka.beginningakka.chapter07.agent;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AgentMain {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("Chapter07");
        ActorRef agentActor = actorSystem.actorOf(Props.create(AgentActor.class), "agentActor");
        agentActor.tell(10, ActorRef.noSender());
    }
}
