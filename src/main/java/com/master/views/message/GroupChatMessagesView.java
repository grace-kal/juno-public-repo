package com.master.views.message;

import com.master.models.messaging.ChatGroup;
import com.master.models.messaging.Message;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.message.ChatGroupService;
import com.master.services.message.MessageService;
import com.master.services.user.UserService;
import com.master.views.MainLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDateTime;
import java.util.*;

@PageTitle("")
@Route(value = "groupChat/:groupName?", layout = MainLayout.class)
@RolesAllowed({"Admin", "User", "Agent"})
public class GroupChatMessagesView extends VerticalLayout implements BeforeEnterObserver {

    private MessageService _messageService;
    private UserService _userService;
    private ChatGroupService _chatGroupService;
    private List<Message> _messages = new ArrayList<Message>();
    private ChatGroup _chatGroup;
    private User _user;
    private TextField _enteredMsgContent = new TextField();
    private FormLayout _enterMsgFormLayout = new FormLayout();

    public GroupChatMessagesView(final MessageService messageService, final UserService userService, final ChatGroupService chatGroupService, AuthenticatedUser authenticatedUser) {
        setSizeFull();
        this._messageService = messageService;
        this._userService = userService;
        this._chatGroupService = chatGroupService;
        getAuthenticatedUser(authenticatedUser);
    }

    private void getAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            _user = maybeUser.get();
        }
        // redirect
        getUI().ifPresent(u -> u.navigate("login"));
    }

    private void initView() {
        H1 groupName = new H1(_chatGroup.getName());
        add(groupName);

        if (!_messages.isEmpty()) {
            _messages.sort((Comparator.comparing(Message::getSentAt)));
//        Display messages
            for (Message msg : _messages) {
                TextField content = new TextField();
                content.setValue(msg.getContent());
                content.getStyle().setFontSize("15px");
                content.setReadOnly(true);

                TextField sentAt = new TextField();
                sentAt.setValue(msg.getSentAt().toString());
                sentAt.getStyle()
                        .setFontSize("12px")
                        .setOpacity("40%")
                        .setFontWeight(Style.FontWeight.LIGHTER);
                sentAt.setReadOnly(true);

                User sender = msg.getSender();
                Avatar avatar = new Avatar(sender.getUsername());
                if (sender.getProfilePicture() != null) {
                    avatar.setImage("images/profilepictures/" + sender.getProfilePicture());
                } else {
                    int colorIndex = new Random().nextInt(2, 20);
                    if (sender.getColorIndex() == 0) sender.setColorIndex(colorIndex);
                    else colorIndex = sender.getColorIndex();
                    avatar.setColorIndex(colorIndex);
                }
                avatar.setThemeName("large");
                avatar.getElement().setAttribute("tabindex", "1");

                TextField sendBy = new TextField();
                sendBy.setValue(msg.getSender().getUsername());
                sendBy.getStyle()
                        .setFontSize("12px")
                        .setOpacity("40%")
                        .setFontWeight(Style.FontWeight.LIGHTER);
                sendBy.setReadOnly(true);

                HorizontalLayout hl = new HorizontalLayout(avatar,sendBy);

                FormLayout messageForm = new FormLayout(hl, sentAt, content);
                if (_user.getId() == msg.getSender().getId()) {
                    messageForm.getStyle().setBorder("2px solid #636ec2");
                } else {
                    messageForm.getStyle().setBorder("2px solid #393E62");
                }
                messageForm.getStyle()
                        .setBorderRadius("15px")
                        .setMargin("1px")
                        .setPadding("10px")
                        .setWidth("40%");

                messageForm.setResponsiveSteps(
                        // Use one column by default
                        new FormLayout.ResponsiveStep("0", 2),
                        // Use two columns, if the layout's width exceeds 320px
                        new FormLayout.ResponsiveStep("320px", 2),
                        // Use three columns, if the layout's width exceeds 500px
                        new FormLayout.ResponsiveStep("500px", 2));

                messageForm.setColspan(content, 2);
                add(messageForm);
            }
        }

//        Create new message
        _enterMsgFormLayout.add(_enteredMsgContent);
        Button enterBtn = new Button("Send");
        enterBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
        enterBtn.addClickListener(e -> {
            if (!_enteredMsgContent.getValue().isEmpty()) {
                Message newMsg = new Message();
                newMsg.setContent(_enteredMsgContent.getValue());
                newMsg.setSentAt(LocalDateTime.now());
                newMsg.setSender(_user);
                newMsg.setReceiverChatGroup(_chatGroup);
                Message returnedMsg = _messageService.create(newMsg);
                _messages.add(returnedMsg);

                _enteredMsgContent.clear();
            }
        });

        HorizontalLayout hl = new HorizontalLayout(_enterMsgFormLayout, enterBtn);
        add(hl);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> groupName = event.getRouteParameters().get("groupName");

        if (groupName.isPresent()) {
            _chatGroup = _chatGroupService.getChatGroupByName(groupName.get());

            List<Message> messages = _messageService.getAllMessagesFromChatGroup(_chatGroup.getId());

            if (!messages.isEmpty()) {
                for (Message msg : messages) {
                    if (msg.getReceiverChatGroup().getName().equals(groupName.get())) {
                        _messages.add(msg);
                    }
                }
            }
        }
        initView();
    }
}
