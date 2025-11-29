package util;

import java.awt.AWTEvent;

import java.awt.event.AWTEventListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue implements AWTEventListener {
    private final BlockingQueue<AWTEvent> queue;

    public void eventDispatched(AWTEvent event) {
        post(event);
    }

    public MessageQueue() {
        queue = new LinkedBlockingQueue<AWTEvent>();
    }

    public void post(AWTEvent event) {
        if (!queue.offer(event)) {
            System.out.println("input ignored!");
        }
    }

    public AWTEvent poll() {
        return queue.poll();

    }

    public void clear() {
        queue.clear();
    }
}
