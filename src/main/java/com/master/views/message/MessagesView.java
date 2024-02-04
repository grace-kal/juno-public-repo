package com.master.views.message;

import com.master.models.messaging.ChatGroup;
import com.master.models.messaging.ChatGroupUserAdmin;
import com.master.models.messaging.Message;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.message.ChatGroupAdminService;
import com.master.services.message.ChatGroupService;
import com.master.services.message.MessageService;
import com.master.services.post.LikeService;
import com.master.services.post.PostService;
import com.master.services.user.UserService;
import com.master.views.MainLayout;
import com.master.views.post.CreatePostDialog;
import com.master.views.post.DetailsView;
import com.master.views.post.ViewPostDialog;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Messages")
@Route(value = "messages", layout = MainLayout.class)
@RolesAllowed({"Admin", "User", "Agent"})
public class MessagesView extends VerticalLayout {

    @Autowired
    private MessageService _messageService;
    @Autowired
    private ChatGroupService _chatGroupService;
    @Autowired
    private ChatGroupAdminService _chatGroupAdminService;

    @Autowired
    private UserService _userService;
    private User _user;

    private VerticalLayout _messagesVerticalLayout = new VerticalLayout();

    public MessagesView(final MessageService messageService,
                        final ChatGroupService chatGroupService,
                        final ChatGroupAdminService chatGroupAdminService,
                        final UserService userService,
                        AuthenticatedUser authenticatedUser) {
        setSizeFull();
        addClassNames("gridwith-filters-view");
        this._chatGroupService = chatGroupService;
        this._messageService = messageService;
        this._chatGroupAdminService = chatGroupAdminService;
        this._userService = userService;
        getAuthenticatedUser(authenticatedUser);
//        this._authenticatedUser = authenticatedUser;
        initView();
    }

    private void initView() {
        createUserMessagesList();
        add(_messagesVerticalLayout);
    }

    private void createUserMessagesList() {
//        All messages sent by user
        List<Message> dms = _messageService.getAllMessagesByUser(_user.getId());
//        All received by user
        dms.addAll(_messageService.getAllMessagesToUser(_user.getId()));
//        All messages from groups that user is part of
        List<ChatGroupUserAdmin> chatGroupUserAdminList = _chatGroupAdminService.getAllChatGroupsOfUser(_user.getId());
//        for (ChatGroupUserAdmin chatGroup : chatGroupUserAdminList) {
//            List<Message> messagesFromUserGroups = _messageService.getAllMessagesFromChatGroup(chatGroup.getChatGroup().getId());
//        }

//Direct messages to users
        H4 dmsSeparator = new H4("Direct messages");
        add(dmsSeparator);

        Button createMessageButton = new Button("Send new message");
        createMessageButton.addClickListener(c -> createNewMessageDialog());
        add(createMessageButton);

        if (!dms.isEmpty()) {
            List<String> listOfUsersToCommunicateTo = new ArrayList<>();
            for (Message message : dms) {
                if (!listOfUsersToCommunicateTo.contains(message.getReceiverUser().getUsername()) &&
                        message.getReceiverUser().getUsername() != _user.getUsername()) {
                    listOfUsersToCommunicateTo.add(message.getReceiverUser().getUsername());
                } else if (!listOfUsersToCommunicateTo.contains(message.getSender().getUsername()) &&
                        message.getSender().getUsername() != _user.getUsername()) {
                    listOfUsersToCommunicateTo.add(message.getSender().getUsername());
                }
            }
            if (!listOfUsersToCommunicateTo.isEmpty()) {
                for (String username : listOfUsersToCommunicateTo) {
                    TextField usernameField = new TextField();
                    usernameField.setValue(username);
                    usernameField.setReadOnly(true);
                    usernameField.getStyle()
                            .setFontSize("14px");

                    Button btnSeeConvo = new Button(LineAwesomeIcon.ARROW_RIGHT_SOLID.create());
                    btnSeeConvo.addClickListener(l -> {
                        openDmsUserView(username);
                    });
                    FormLayout singleChatForm = new FormLayout(usernameField, btnSeeConvo);
                    singleChatForm.addClickListener(v -> openDmsUserView(username));
                    singleChatForm.getStyle().setBorder("2px solid #636ec2").setBorderRadius("15px");
                    add(singleChatForm);
                }
            }
        }

//        Group chat messages
        H4 groupChatSeparator = new H4("Group chats");
        add(groupChatSeparator);

        HorizontalLayout createSearchChatGroupHL = new HorizontalLayout();
        Button createChatGroupButton = new Button("Create new chat group");
        createChatGroupButton.addClickListener(c -> createNewChatGroupDialog());
        createSearchChatGroupHL.add(createChatGroupButton);

        Button searchChatGroupButton = new Button(LineAwesomeIcon.SEARCH_SOLID.create());
        searchChatGroupButton.addClickListener(c -> searchChatGroupDialog());
        createSearchChatGroupHL.add(searchChatGroupButton);

        add(createSearchChatGroupHL);

        if (!chatGroupUserAdminList.isEmpty()) {
            List<ChatGroupUserAdmin> listOfGroupsToCommunicateTo = new ArrayList<>();

            for (ChatGroupUserAdmin chatGroupUserAdmin : chatGroupUserAdminList) {
                if (!listOfGroupsToCommunicateTo.contains(chatGroupUserAdmin)) {
                    listOfGroupsToCommunicateTo.add(chatGroupUserAdmin);
                }
            }

            for (ChatGroupUserAdmin chatGroup : listOfGroupsToCommunicateTo) {
                TextField chatGroupName = new TextField();
                chatGroupName.setValue(chatGroup.getChatGroup().getName());
                chatGroupName.setReadOnly(true);
                chatGroupName.getStyle()
                        .setFontSize("14px");

                Button btnSeeConvo = new Button(LineAwesomeIcon.ARROW_RIGHT_SOLID.create());
                btnSeeConvo.addClickListener(l -> {
                    openChatGroupMessagesView(chatGroup.getChatGroup().getName());
                });
                FormLayout singleChatForm = new FormLayout(chatGroupName, btnSeeConvo);
                singleChatForm.addClickListener(v -> openChatGroupMessagesView(chatGroup.getChatGroup().getName()));
                singleChatForm.getStyle().setBorder("2px solid #636ec2").setBorderRadius("15px");
                add(singleChatForm);
            }
        }
    }

    private void openChatGroupMessagesView(String chatgroupName) {
        getUI().ifPresent(ui -> ui.navigate(
                GroupChatMessagesView.class,
                new RouteParameters("groupName", chatgroupName)));
    }

    private void openDmsUserView(String usernameToComminicateTo) {
        getUI().ifPresent(ui -> ui.navigate(
                DmView.class,
                new RouteParameters("usernameToCommunicateTo", usernameToComminicateTo)));
    }

    private void createNewChatGroupDialog() {
        CreateNewChatGroupDialog createNewChatGroupDialog = new CreateNewChatGroupDialog(_chatGroupService, _chatGroupAdminService, _user);
        createNewChatGroupDialog.open();
        createNewChatGroupDialog.addConfirmListener(ll -> createUserMessagesList());
    }

    private void createNewMessageDialog() {
        CreateNewMessageDialog createMessageDialog = new CreateNewMessageDialog(_messageService, _userService, _user);
        createMessageDialog.open();
        createMessageDialog.addConfirmListener(ll -> createUserMessagesList());
    }

    private void searchChatGroupDialog() {
        SearchChatGroupDialog dialog = new SearchChatGroupDialog(_chatGroupService, _chatGroupAdminService, _user);
        dialog.open();
        dialog.addConfirmListener(ll -> createUserMessagesList());
    }

    private void getAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            _user = maybeUser.get();
        }
        // redirect
        getUI().ifPresent(u -> u.navigate("login"));
    }
}

