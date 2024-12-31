package com.microtask.msghandler.entity;
import com.microtask.msghandler.config.StringEncryptionConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "messages")
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column
    private String tag;

    @Column
    @Convert(converter = StringEncryptionConverter.class)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date createDt;

    public MessageEntity(String msg, String tag){
        this.message = msg;
        this.tag = tag;
    }
}
