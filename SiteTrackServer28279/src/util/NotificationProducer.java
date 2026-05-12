package util;

/**
 * Stub implementation of NotificationProducer to allow the service layer to compile.
 * This should eventually send messages to a message broker (e.g., ActiveMQ/RabbitMQ).
 */
public class NotificationProducer {
    public static void sendNotification(String userId, String eventType, String message, String channel) {
        System.out.println(">>> [NOTIFICATION PRODUCER] Triggered " + eventType + " for User " + userId + " on channel " + channel + " | " + message);
        // In a real implementation, this wraps the message and drops it into an MQ queue
    }
}
