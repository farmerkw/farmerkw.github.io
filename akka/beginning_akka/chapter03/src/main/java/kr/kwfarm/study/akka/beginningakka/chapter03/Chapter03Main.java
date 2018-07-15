package kr.kwfarm.study.akka.beginningakka.chapter03;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Chapter03Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Chapter03");
        ActorRef ping = actorSystem.actorOf(Props.create(PingActor.class), "pingActor");
        ping.tell("work", ActorRef.noSender());
    }
}
