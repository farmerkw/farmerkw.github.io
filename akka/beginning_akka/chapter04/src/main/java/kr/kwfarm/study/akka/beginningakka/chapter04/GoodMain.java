package kr.kwfarm.study.akka.beginningakka.chapter04;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class GoodMain {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Chapter04");
        ActorRef ping = actorSystem.actorOf(Props.create(PingActor.class));
        ping.tell("good", ActorRef.noSender());
    }
}
