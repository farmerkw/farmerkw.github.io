package kr.kwfarm.study.akka.beginningakka.chaptr02;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Chapter02Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Kwfarm");
        ActorRef ping = actorSystem.actorOf(Props.create(PingActor.class), "pingActor");
        ping.tell("start", ActorRef.noSender());

    }
}
