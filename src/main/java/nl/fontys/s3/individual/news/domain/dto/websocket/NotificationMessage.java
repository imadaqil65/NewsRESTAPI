package nl.fontys.s3.individual.news.domain.dto.websocket;

import lombok.Data;

@Data
public class NotificationMessage {
    private String id;
    private String title;
    private String text;
}
