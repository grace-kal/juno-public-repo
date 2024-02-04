package com.master.views.post;

import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.post.PostService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;

public class CreatePostDialog extends ConfirmDialog {
    private Post _post = new Post();
    private User _user = new User();
    private PostService _postService;
    private Binder<Post> _binder = new Binder<Post>();

    private TextField _title = new TextField("Title");
    private TextField _content = new TextField("Content");

    public CreatePostDialog(PostService postService, User user) {
        _postService = postService;
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

        FormLayout formLayout = new FormLayout(_title, _content);

        _title.setEnabled(true);
        _content.setEnabled(true);
        _binder.forField(_title).bind(Post::getTitle, Post::setTitle);
        _binder.forField(_content).bind(Post::getContent, Post::setContent);
        _binder.readBean(_post);
        formLayout.setSizeFull();
        add(formLayout);

        setConfirmText("Create");
        setCloseOnEsc(true);
//        setRejectable(true);
//        setRejectText("Close");

        addConfirmListener(l -> {
            try {
                _binder.writeBean(_post);
                _post.setAuthor(_user);
                _post.setCreatedAt(LocalDateTime.now());
                _postService.create(_post);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
