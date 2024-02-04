package com.master.views.user;

import com.github.javaparser.utils.Log;
import com.master.models.user.User;
import com.master.services.user.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@AnonymousAllowed
@PageTitle("Register")
@Route(value = "register")
public class RegisterView extends VerticalLayout {
    private UserService _userService;
    private User _user = new User();
    private Binder<User> _binder;
    private PasswordEncoder _encoder;

    @Autowired
    public RegisterView(final UserService userService, PasswordEncoder encoder) {
        this._userService = userService;
        this._encoder = encoder;
        initView();
    }

    public void initView() {
        H1 title = new H1("Register");
        add(title);

        EmailField email = new EmailField("Email");
        email.setRequired(true);
        TextField username = new TextField("Username");
        username.setRequired(true);
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        IntegerField age = new IntegerField("Age");
        age.setMin(0);
        age.setRequired(true);
        DatePicker dob = new DatePicker("Date of birth");
        PasswordField password = new PasswordField("Create password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
//        created, updated set when successful register
//        set role User default

        _binder = new Binder<>(User.class);
        _binder.forField(email)
                .asRequired("Enter email!")
                .withValidator(Validator.from(v -> v != null && _userService.findUserByEmail(v) == null, "The email is already taken!"))
                .bind(User::getEmail, User::setEmail);
        _binder.forField(username)
                .asRequired("Enter a username!")
                .withValidator(Validator.from(v -> v != null && _userService.findUserByUsername(v) == null, "The username is already taken!"))
                .bind(User::getUsername, User::setUsername);
        _binder.forField(firstName).bind(User::getFirstName, User::setFirstName);
        _binder.forField(lastName).bind(User::getLastName, User::setLastName);
        _binder.forField(age).asRequired("Enter valid age!").bind(User::getAge, User::setAge);
        _binder.forField(dob).bind(User::getDob, User::setDob);
        _binder.forField(password).bind(User::getPassword, User::setPassword);
        _binder.forField(confirmPassword)
                .asRequired()
                .withValidator(Validator.from(v -> v != null && Objects.equals(v, password.getValue()), "The passwords don't match!"))
                .bind("password");
        _binder.readBean(_user);

        FormLayout formLayout = new FormLayout(email, username, firstName, lastName, age, dob, password, confirmPassword);

        formLayout.setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 2),
                // Use two columns, if the layout's width exceeds 320px
                new ResponsiveStep("320px", 2),
                // Use three columns, if the layout's width exceeds 500px
                new ResponsiveStep("500px", 2));
        formLayout.setColspan(username, 2);
        formLayout.setColspan(email, 2);
        formLayout.setColspan(password, 2);
        formLayout.setColspan(confirmPassword, 2);
        formLayout.setWidth("30%");

        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER, formLayout);

        add(formLayout);

        Button registerButton = new Button("Register");
        registerButton.addClickListener(b -> {
            try {
                _binder.writeBean(_user);
                String encryptedPassword = _encoder.encode(_user.getPassword());
                _user.setPassword(encryptedPassword);
                _user.setCreatedAt(LocalDateTime.now());
                _user.setUpdatedAt(LocalDateTime.now());
                _userService.create(_user);
                // redirect
                getUI().ifPresent(u->u.navigate(""));
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
        add(registerButton);
        Anchor loginLink = new Anchor("login", "Sign in");
        add(loginLink);

//        SplitLayout splitLayout = new SplitLayout(formLayout, new Div());
//        add(splitLayout);

    }
}
