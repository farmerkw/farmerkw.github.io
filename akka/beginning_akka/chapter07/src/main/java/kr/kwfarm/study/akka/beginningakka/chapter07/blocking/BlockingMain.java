package kr.kwfarm.study.akka.beginningakka.chapter07.blocking;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class BlockingMain {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("Chapter07");
        ActorRef blockingActor = actorSystem.actorOf(Props.create(BlockingActor.class), "blockingActor");
        blockingActor.tell(10, ActorRef.noSender());
        blockingActor.tell("hello", ActorRef.noSender());
    }
}
