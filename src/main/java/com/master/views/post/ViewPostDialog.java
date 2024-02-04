package com.master.views.post;

import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.services.post.PostService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDateTime;
import java.util.Objects;

public class ViewPostDialog extends ConfirmDialog {

    private Post _post;
    private PostService _postService;
    private Binder<Post> _binder = new Binder<Post>();

    private TextField _title = new TextField("Title");
    private TextField _content = new TextField("Content");
    private String _status;


    public ViewPostDialog(Post post, PostService postService) {
        _post = post;
        _postService = postService;
        initView();
    }

    private void initView() {
//       Dropdown menu
        MenuBar postMenu = new MenuBar();
        postMenu.setThemeName("tertiary-inline contrast");

        MenuItem postMenuItem = postMenu.addItem("");
        Div div = new Div();
        div.add(new Icon("lumo", "dropdown"));
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("align-items", "center");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        postMenuItem.add(div);
        postMenuItem.getSubMenu()
                .addItem("View details", e -> {
                    setConfirmText("Details");
                    _status = "Details";
                });
        postMenuItem.getSubMenu()
                .addItem("Edit post", e -> {
                    _title.setEnabled(true);
                    _content.setEnabled(true);
                    _status = "Edit";
                    setConfirmText("Edit");
                });
        postMenuItem.getSubMenu()
                .addItem("Delete post", e -> {
                    setConfirmText("Delete");
                    _status = "Delete";
                });
        postMenu.getStyle().setAlignItems(Style.AlignItems.END);

//Close button
        Button closeBtn = new Button("x");
        closeBtn.addClickListener(l -> {
            close();
        });
//        Adding drop down and close button in one
        HorizontalLayout horizontalLayout = new HorizontalLayout(postMenu, closeBtn);
        horizontalLayout.setSizeFull();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.END);
        add(horizontalLayout);

//        Items from the actual post
        FormLayout formLayout = new FormLayout(_title, _content);
        _title.setEnabled(false);
        _content.setEnabled(false);
        _binder.forField(_title).bind(Post::getTitle, Post::setTitle);
        _binder.forField(_content).bind(Post::getContent, Post::setContent);
        _binder.readBean(_post);
        formLayout.setSizeFull();
        add(formLayout);


        setCloseOnEsc(true);
        setConfirmText("Close");
//        setRejectText("Close");
//        addRejectListener(l -> {
//            close();
//        });
        addConfirmListener(l -> {
            if (Objects.equals(_status, "Edit")) {
                try {
                    _binder.writeBean(_post);
                    _post.setId(_postService.getById(_post.getId()).getId());
                    _postService.update(_post);
                } catch (ValidationException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(_status, "Delete")) {
                try {
                    _postService.remove(_postService.getById(_post.getId()).getId());
                    getUI().ifPresent(ui -> ui.navigate(
                            FeedView.class));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(_status, "Details")) {
//                getUI().ifPresent(u -> u.navigate("postdetails/postId=" + _post.getId()));
                getUI().ifPresent(ui -> ui.navigate(
                        DetailsView.class,
                        new RouteParameters("postId", _post.getId().toString())));
            }
        });
    }
}
