package com.master.views.user.admin;

import com.master.models.enums.Roles;
import com.master.models.user.User;
import com.master.services.user.UserService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;

public class UpdateUserDialog extends ConfirmDialog {

    private UserService _userService;
    private User _user;
    private Binder<User> _binder = new Binder<User>();

    private TextField username = new TextField("Username");
    private EmailField email = new EmailField("Email");
    private Checkbox isRestricted = new Checkbox("Restricted?");
    private RadioButtonGroup<Roles> roles = new RadioButtonGroup<>("Roles");

    public UpdateUserDialog(User user, final UserService userService) {
        _user = user;
        _userService = userService;
        initView();
    }

    private void initView() {
        setHeader("Update user");

        updateUserForm();

        addConfirmListener(l -> {
            try {
                _binder.writeBean(_user);
                _user.setId(_userService.findUserByUsername(_user.getUsername()).getId());
                _user.setUpdatedAt(LocalDateTime.now());
                _userService.update(_user);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateUserForm(){
        roles.setItems(Roles.values());
        setConfirmText("Save");

        FormLayout formLayout = new FormLayout(username, email, isRestricted, roles);
        _binder.forField(username).bind(User::getUsername,User::setUsername);
        _binder.forField(email).bind(User::getEmail,User::setEmail);
        _binder.forField(roles).bind(User::getRole,User::setRole);
        _binder.forField(isRestricted).bind(User::isRestricted,User::setRestricted);
        _binder.readBean(_user);
        formLayout.setSizeFull();
        add(formLayout);
    }
}
