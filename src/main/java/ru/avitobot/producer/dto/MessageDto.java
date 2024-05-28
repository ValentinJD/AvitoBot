package ru.avitobot.producer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto implements Serializable {

    @JsonProperty("chats")
    private List<Chat> chats = new ArrayList<>();

}
