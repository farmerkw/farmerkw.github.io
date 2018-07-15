package kr.kwfarm.study.akka.beginningakka.chapter08;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;

public class Node01Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("ClusterSystem");
        ActorRef serviceRouter = actorSystem.actorOf(Props.create(PingService.class)
                .withRouter(new FromConfig()), "serviceRouter");
        ActorRef httpActor = actorSystem.actorOf(Props.create(HttpActor.class, serviceRouter), "httpActor");
    }
}
