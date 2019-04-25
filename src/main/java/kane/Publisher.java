package kane;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Subscriber> subscribers;

    public Publisher() {
        this.subscribers = new ArrayList<>();
    }

    public void send(String event) {
        for (Subscriber s : subscribers) {
            try {
                s.receive(event);
            } catch(Exception e) {}
        }
    }
}
