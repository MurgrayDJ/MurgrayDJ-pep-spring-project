package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.List;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.CustomException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;
    
    SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping(value = "/register")
    // @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        Optional<Account> accountOptional = accountService.getAccount(account);
        if(accountOptional.isEmpty()){
            if(account.getUsername().length() > 0 && account.getPassword().length() > 4 ){
                Optional<Account> newAccountOptional = accountService.addAccount(account);
                if(newAccountOptional.isPresent()){
                    Account newAccount = newAccountOptional.get();
                    return ResponseEntity.status(HttpStatus.OK).body(newAccount); //Account created successfully
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //Account creation failed
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build(); //Account already exists
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Optional<Account> accountOptional = accountService.getAccount(account);
        if(accountOptional.isPresent()){
            Account foundAccount = accountOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(foundAccount); //Login successful
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //Login failed
    }

    @PostMapping(value = "/messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message message){ 
        int msgTextLength = message.getMessage_text().length();
        if(msgTextLength > 0 && msgTextLength < 255){
            Optional<Account> accountOptional = accountService.findAccountById(message.getPosted_by());
            if(accountOptional.isPresent()){
                Optional<Message> messageOptional = messageService.postMessage(message); 
                if(messageOptional.isPresent()){
                    Message postedMessage = messageOptional.get();
                    return ResponseEntity.status(HttpStatus.OK).body(postedMessage); //Message posted successfully
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //Message post failed
    }

    @GetMapping(value = "/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.findAllMessages(); //Returns a list, may be empty
        return ResponseEntity.status(HttpStatus.OK).body(messages); 
    }

    @GetMapping(value = "/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") int message_id){
        Optional<Message> messageOptional = messageService.findMessageById(message_id); 
        if(messageOptional.isPresent()){
            Message foundMessage = messageOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(foundMessage); //Message found
        }
        return ResponseEntity.status(HttpStatus.OK).build(); //Message not found
    }

    @DeleteMapping(value = "/messages/{message_id}")
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Integer> deleteMessage(@PathVariable("message_id") Integer message_id){
        if(message_id == null || messageService.findMessageById(message_id).isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).build(); //Message deletion fails
        }
        
        messageService.deleteMessageById(message_id);
        return ResponseEntity.status(HttpStatus.OK).body(1); //Message deletion succeeds
    }

    @PatchMapping(value = "/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable("message_id") Integer message_id, @RequestBody Message message){
        if(message_id != null 
        && message.getMessage_text().length() > 0
        && message.getMessage_text().length() < 255){
            Optional<Integer> numMessagesUpdated = messageService.updateMessageById(message_id, message);
            if(numMessagesUpdated.isPresent()){
                int numMessages = numMessagesUpdated.get();
                return ResponseEntity.status(HttpStatus.OK).body(numMessages); //Message update succeeds
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //Message update failed
    }

    @GetMapping(value = "/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesFromUser(@PathVariable("account_id") Integer account_id){
        List<Message> messages = messageService.findAllMessagesByUser(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messages); //Returns a list, may be empty
    }
}
