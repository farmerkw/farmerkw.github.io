package kr.kwfarm.study.akka.beginningakka.chapter08;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HttpActor extends UntypedConsumerActor {
    private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private String uri;

    private ActorRef service;

    private ActorRef sender;

    public HttpActor(ActorRef service) {
        this.service = service;
        this.uri = "jetty:http://localhost:8877/akkaStudy";
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof CamelMessage) {
            this.sender = getSender();
            service.tell("Hello", getSelf());
        } else if (message instanceof String) {
            this.sender.tell(message, getSelf());
        } else {
            unhandled(message);
        }
    }

    @Override
    public String getEndpointUri() {
        return uri;
    }
}
