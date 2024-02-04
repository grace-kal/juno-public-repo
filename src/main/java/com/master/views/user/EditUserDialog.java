package com.master.views.user;

import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.services.user.UserService;
import com.master.views.post.DetailsView;
import com.master.views.post.FeedView;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.RouteParameters;

import java.util.Objects;
import java.util.Random;

public class EditUserDialog extends ConfirmDialog {

    private User _user;
    private UserService _service;
    private Binder<User> _binder = new Binder<User>();

    public EditUserDialog(User user, UserService service) {
        _user = user;
        _service = service;
        initView();
    }

    private void initView() {
//        Close dialog btn
        Button closeBtn = new Button("x");
        closeBtn.addClickListener(l -> {
            close();
        });

//        Items for the user profile
        TextField username = new TextField();
        username.setValue(_user.getUsername());
        username.getStyle().setFontSize("20px");

        TextField fname = new TextField();
        String fnameFromDb = _user.getFirstName();
        if(fnameFromDb !=null){
            fname.setValue(_user.getFirstName());
        }
        fname.getStyle().setFontSize("15px");

        TextField lname = new TextField();
        String lnameFromDb = _user.getLastName();
        if(lnameFromDb !=null){
            lname.setValue(_user.getLastName());
        }
        lname.getStyle().setFontSize("15px");

        TextField age = new TextField();
        age.setValue(String.valueOf(_user.getAge()));
        age.getStyle().setFontSize("15px");

        TextField email = new TextField();
        email.setValue(_user.getEmail());
        email.getStyle().setFontSize("15px");

        Avatar avatar = new Avatar(_user.getUsername());
        if (_user.getProfilePicture() != null) {
            avatar.setImage("images/profilepictures/" + _user.getProfilePicture());
        } else {
            int colorIndex = new Random().nextInt(2, 20);
            if (_user.getColorIndex() == 0) _user.setColorIndex(colorIndex);
            else colorIndex = _user.getColorIndex();
            avatar.setColorIndex(colorIndex);
        }
        avatar.setThemeName("large");
        avatar.getElement().setAttribute("tabindex", "1");
        Button selectAvatartBtn = new Button("Edit avatar");
        selectAvatartBtn.addClickListener(l -> {

        });

        TextField avatarPath = new TextField();
        avatarPath.setValue(_user.getProfilePicture());
        avatarPath.setVisible(false);

        _binder.forField(username).bind(User::getUsername, User::setUsername);
        _binder.forField(fname).bind(User::getFirstName, User::setFirstName);
        _binder.forField(lname).bind(User::getLastName, User::setLastName);
        _binder.forField(email).bind(User::getEmail, User::setEmail);
        _binder.forField(avatarPath).bind(User::getProfilePicture, User::setProfilePicture);

        _binder.readBean(_user);

        FormLayout formLayout = new FormLayout(avatar, selectAvatartBtn, avatarPath, username, fname, lname, email, age);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 2),
                // Use two columns, if the layout's width exceeds 320px
                new FormLayout.ResponsiveStep("320px", 2),
                // Use three columns, if the layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.setWidth("90%");
        formLayout.getStyle().setBorderRadius("15px");
        formLayout.getStyle().setMargin("1px");
        formLayout.getStyle().setPadding("10px");

        formLayout.setColspan(avatar, 2);
        formLayout.setColspan(selectAvatartBtn, 2);
        formLayout.setColspan(username, 2);
        add(formLayout);

        addConfirmListener(l -> {
            try {
                _binder.writeBean(_user);
                _user.setId(_service.getById(_user.getId()).getId());
                _service.update(_user);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
