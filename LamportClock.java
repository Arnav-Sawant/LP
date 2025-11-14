import java.util.*;

class Process {
    int pid;        // Process ID
    int clock;      // Lamport Logical Clock

    public Process(int pid) {
        this.pid = pid;
        this.clock = 0;
    }

    // Internal event (computation/local event)
    public void internalEvent() {
        clock++;
        System.out.println("Process " + pid + " internal event. Clock = " + clock);
    }

    // Send a message
    public int sendEvent() {
        clock++;
        System.out.println("Process " + pid + " sends message. Clock = " + clock);
        return clock;
    }

    // Receive a message from another process with timestamp
    public void receiveEvent(int timestamp) {
        clock = Math.max(clock, timestamp) + 1;
        System.out.println("Process " + pid + " receives message. Clock = " + clock);
    }
}

public class LamportClock {
    public static void main(String[] args) {
        Process p1 = new Process(1);
        Process p2 = new Process(2);

        // P1 internal event
        p1.internalEvent();

        // P1 sends message to P2
        int msgTimestamp = p1.sendEvent();

        // P2 receives message
        p2.receiveEvent(msgTimestamp);

        // P2 internal event
        p2.internalEvent();

        // P2 sends reply to P1
        int replyTimestamp = p2.sendEvent();
        p1.receiveEvent(replyTimestamp);
    }
}
