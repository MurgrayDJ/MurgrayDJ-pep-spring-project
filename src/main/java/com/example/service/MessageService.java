package com.example.service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.MessageRepository;
import com.example.entity.Message;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Optional<Message> findMessageById(int message_id){
        Optional<Message> messageOptional = messageRepository.findById(message_id);
        return messageOptional;
    }

    public Optional<Message> postMessage(Message message){
        return Optional.of(messageRepository.save(message));
    }

    public List<Message> findAllMessages(){
        return messageRepository.findAll();
    }

    public void deleteMessageById(int message_id){
        messageRepository.deleteById(message_id);
    }

    public Optional<Integer> updateMessageById(int message_id, Message message){
        Optional<Message> oldMessageOptional = messageRepository.findById(message_id);
        if(oldMessageOptional.isPresent()){
            Message oldMessage = oldMessageOptional.get(); 
            oldMessage.setMessage_text(message.getMessage_text());
            return Optional.of(1);
        }
        return Optional.empty();
    }

    public List<Message> findAllMessagesByUser(int account_id){
        return messageRepository.findAllByPostedBy(account_id);
    }
}
