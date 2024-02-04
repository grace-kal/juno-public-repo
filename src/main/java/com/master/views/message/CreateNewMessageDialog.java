package com.master.views.message;

import com.master.models.messaging.Message;
import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.services.message.MessageService;
import com.master.services.post.PostService;
import com.master.services.user.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.awt.*;
import java.time.LocalDateTime;

public class CreateNewMessageDialog extends ConfirmDialog {

    private TextField _content = new TextField("Content");
    private TextField _usernameOfReceiver = new TextField("Receiver");
    private Text _errorFindingUser = new Text("");
    private Message _message = new Message();
    private Binder<Message> _binder = new Binder<Message>();
    private User _user;
    private MessageService _messageService;
    private UserService _userService;

    public CreateNewMessageDialog(MessageService messageService, UserService userService, User user) {
        _messageService = messageService;
        _userService = userService;
        _user = user;
        initView();
    }

    private void initView() {
        //Close button
        Button closeBtn = new Button("x");
        closeBtn.addClickListener(l -> {
            close();
        });
        add(closeBtn);

        createMessageForm();
    }

    private void createMessageForm() {

        Button searchBtn = new Button("Search");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
        searchBtn.addClickListener(e -> searchBtn());

        FormLayout formLayout = new FormLayout(_content,_errorFindingUser,_usernameOfReceiver,searchBtn);

        _content.setEnabled(true);
        _binder.forField(_content).bind(Message::getContent, Message::setContent);

        _binder.readBean(_message);
        formLayout.setSizeFull();
        add(formLayout);

        setConfirmText("Send");
        setCloseOnEsc(true);
//        setRejectable(true);
//        setRejectText("Close");

        addConfirmListener(l -> {
            try {
                _binder.writeBean(_message);
                _message.setSender(_user);
                _message.setSentAt(LocalDateTime.now());


                User receiver = _userService.findUserByUsername(_usernameOfReceiver.getValue());
                if(receiver!=null){
                    _errorFindingUser.setText("");
                    _message.setReceiverUser(_userService.findUserByUsername(_usernameOfReceiver.getValue()));
                    _messageService.create(_message);

                }
                else {
                    _errorFindingUser.setText("No such username");
                }
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void searchBtn() {
        User receiver = _userService.findUserByUsername(_usernameOfReceiver.getValue());
        if(receiver!=null){
            _errorFindingUser.setText("");
            _usernameOfReceiver.setValue(receiver.getUsername());
        }
        else {
            _errorFindingUser.setText("No such username");
        }
    }
}
