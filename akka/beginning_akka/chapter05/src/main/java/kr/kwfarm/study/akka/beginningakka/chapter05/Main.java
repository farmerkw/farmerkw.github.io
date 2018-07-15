package kr.kwfarm.study.akka.beginningakka.chapter05;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Chapter05");
        ActorRef ping = actorSystem.actorOf(Props.create(PingActor.class), "pingActor");

        ping.tell("work", ActorRef.noSender());
        ping.tell("reset", ActorRef.noSender());
    }
}
