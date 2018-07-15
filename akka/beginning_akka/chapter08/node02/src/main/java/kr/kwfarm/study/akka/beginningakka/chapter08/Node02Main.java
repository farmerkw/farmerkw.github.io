package kr.kwfarm.study.akka.beginningakka.chapter08;

import akka.actor.ActorSystem;

public class Node02Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("ClusterSystem");
    }
}
