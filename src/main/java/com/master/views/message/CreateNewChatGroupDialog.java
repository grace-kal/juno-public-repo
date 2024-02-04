package com.master.views.message;

import com.master.models.messaging.ChatGroup;
import com.master.models.messaging.ChatGroupUserAdmin;
import com.master.models.messaging.Message;
import com.master.models.user.User;
import com.master.services.message.ChatGroupAdminService;
import com.master.services.message.ChatGroupService;
import com.master.services.message.MessageService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CreateNewChatGroupDialog extends ConfirmDialog {
    private TextField _name = new TextField("Name");
    private TextField _description = new TextField("Description");
    private Checkbox _isPrivateGroup = new Checkbox("Is private group");
    private ChatGroup _chatGroup = new ChatGroup();
    private Binder<ChatGroup> _binder = new Binder<ChatGroup>();
    private User _user = new User();
    private ChatGroupService _chatGroupService;
    private ChatGroupAdminService _chatGroupUserAdminService;

    public CreateNewChatGroupDialog(final ChatGroupService chatGroupService, ChatGroupAdminService chatGroupAdminService, User user) {
        _chatGroupService = chatGroupService;
        this._chatGroupUserAdminService = chatGroupAdminService;
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

        FormLayout formLayout = new FormLayout(_name, _description, _isPrivateGroup);

        _name.setEnabled(true);
        _binder.forField(_name).bind(ChatGroup::getName, ChatGroup::setName);

        _description.setEnabled(true);
        _binder.forField(_description).bind(ChatGroup::getDescription, ChatGroup::setDescription);

        _isPrivateGroup.setEnabled(true);
        _binder.forField(_isPrivateGroup).bind(ChatGroup::isPrivate, ChatGroup::setPrivate);

        _binder.readBean(_chatGroup);
        formLayout.setSizeFull();
        add(formLayout);

        setConfirmText("Send");
        setCloseOnEsc(true);
//        setRejectable(true);
//        setRejectText("Close");

        addConfirmListener(l -> {
            try {
//                Creating the chat group
                _binder.writeBean(_chatGroup);
                _chatGroup.setCreatedAt(LocalDateTime.now());
                ChatGroup newChatGroup = _chatGroupService.create(_chatGroup);
//                Adding the creator as admin and creator
                ChatGroupUserAdmin admin = new ChatGroupUserAdmin();
                admin.setUser(_user);
                admin.setAdmin(true);
                admin.setTheCreator(true);
                admin.setChatGroup(newChatGroup);
                _chatGroupUserAdminService.create(admin);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
