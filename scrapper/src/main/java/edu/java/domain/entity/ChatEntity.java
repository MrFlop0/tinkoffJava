package edu.java.domain.entity;

import edu.java.domain.dto.Chat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "chat")
public class ChatEntity {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @ManyToMany
    @JoinTable(name = "links_chats",
               joinColumns = @JoinColumn(name = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "link"))
    private Set<LinkEntity> links = new HashSet<>();

    public Chat toDto() {
        return new Chat(chatId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof ChatEntity chat) {
            return Objects.equals(this.chatId, chat.chatId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
