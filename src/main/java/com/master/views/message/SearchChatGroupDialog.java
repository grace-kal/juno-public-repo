package com.master.views.message;

import com.master.models.messaging.ChatGroup;
import com.master.models.messaging.ChatGroupUserAdmin;
import com.master.models.messaging.Message;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.message.ChatGroupAdminService;
import com.master.services.message.ChatGroupService;
import com.master.services.message.MessageService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SearchChatGroupDialog extends ConfirmDialog {
    private TextField _chatGroupName = new TextField("Chat group name");
    private Text _errorFindingGroup = new Text("");


    private User _user;
    private ChatGroupService _chatGroupService;
    private ChatGroupAdminService _chatGroupUsersService;

    public SearchChatGroupDialog(ChatGroupService chatGroupService, ChatGroupAdminService chatGroupAdminService, User user) {
        _chatGroupService = chatGroupService;
        _chatGroupUsersService = chatGroupAdminService;
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

        Button searchBtn = new Button("Search");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
        searchBtn.addClickListener(e -> searchBtn());

        FormLayout formLayout = new FormLayout(_errorFindingGroup, _chatGroupName, searchBtn);

        _chatGroupName.setEnabled(true);
        formLayout.setSizeFull();
        add(formLayout);

        setConfirmText("Join chat group");
        setCloseOnEsc(true);

        addConfirmListener(l -> {
            ChatGroup charGroup = _chatGroupService.getChatGroupByName(_chatGroupName.getValue());
            if (charGroup != null) {
                List<ChatGroupUserAdmin> chatGroupUserAdminList = _chatGroupUsersService.getAllChatGroupsOfUser(_user.getId());
                boolean userIdAlreadyInGroup=false;
                if(chatGroupUserAdminList!=null){
                    for (ChatGroupUserAdmin item : chatGroupUserAdminList) {
                        if (Objects.equals(item.getUser().getId(), _user.getId()) && item.getChatGroup().getId() == charGroup.getId())
                            userIdAlreadyInGroup = true;
                    }
                }
                if(!userIdAlreadyInGroup){
                    _errorFindingGroup.setText("");
                    ChatGroupUserAdmin item = new ChatGroupUserAdmin();
                    item.setChatGroup(charGroup);
                    item.setUser(_user);
                    item.setTheCreator(false);
                    item.setAdmin(false);
                    _chatGroupUsersService.create(item);
                }
                else{
                    _errorFindingGroup.setText("You're already in this chat group.");
                }
            } else {
                _errorFindingGroup.setText("No such chat group");
            }
        });

    }

    private void searchBtn() {
        ChatGroup chatGroup = _chatGroupService.getChatGroupByName(_chatGroupName.getValue());
        if (chatGroup != null && !chatGroup.isPrivate()) {
            _errorFindingGroup.setText("");
            _chatGroupName.setValue(chatGroup.getName());
        } else {
            _errorFindingGroup.setText("No such chat group.");
        }
    }

}
