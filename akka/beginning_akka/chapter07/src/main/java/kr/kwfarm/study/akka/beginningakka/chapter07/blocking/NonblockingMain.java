package kr.kwfarm.study.akka.beginningakka.chapter07.blocking;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class NonblockingMain {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("Chapter07");
        ActorRef nonBlockingActor = actorSystem.actorOf(Props.create(NonblockingActor.class), "nonblockingActor");
        nonBlockingActor.tell(10, ActorRef.noSender());
        nonBlockingActor.tell("hello", ActorRef.noSender());
    }
}
