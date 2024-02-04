package com.master.views.post;

import com.master.models.messaging.Message;
import com.master.models.post.Like;
import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.post.LikeService;
import com.master.services.post.PostService;
import com.master.views.MainLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Feed")
@Route(value = "feed", layout = MainLayout.class)
@RolesAllowed({"Admin", "User", "Agent"})
public class FeedView extends VerticalLayout {
    @Autowired
    private PostService _postService;
    @Autowired
    private LikeService _likeService;

    private VerticalLayout _postVerticalLayout = new VerticalLayout();
    private User _user;
    AtomicReference<List<Post>> _postList;

    public FeedView(final PostService postService, final LikeService likeService, AuthenticatedUser authenticatedUser) {
        setSizeFull();
        addClassNames("gridwith-filters-view");
        this._postService = postService;
        this._likeService = likeService;
        getAuthenticatedUser(authenticatedUser);
//        this._authenticatedUser = authenticatedUser;
        initView();
    }

    private void initView() {
        Button createButton = new Button("Create new post");
        createButton.addClickListener(c -> createPostDialog());
        add(createButton);
        createUserPostsList();
        add(_postVerticalLayout);
    }

    private void createUserPostsList() {
//        if(!(_postList.get() ==null)&& !_postList.get().isEmpty()) {
//            _postList.set();
//        }
        _postList = new AtomicReference<>(_postService.getAll());
        if (!_postList.get().isEmpty()) {
            _postList.get().sort((Comparator.comparing(Post::getCreatedAt)));

            for (Post post : _postList.get()) {
                TextField title = new TextField();
                title.setValue(post.getTitle());
                title.setReadOnly(true);
                title.getStyle()
                        .setFontSize("36px");

                TextArea content = new TextArea();
                content.setValue(post.getContent());
                content.setReadOnly(true);

                TextField createdDate = new TextField();
                createdDate.setValue(post.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                createdDate.setReadOnly(true);
                createdDate.getStyle()
                        .setFontSize("12px")
                        .setOpacity("40%")
                        .setFontWeight(Style.FontWeight.LIGHTER);

                User sender = post.getAuthor();
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

                TextField author = new TextField();
                author.setValue(post.getAuthor().getUsername());
                author.setReadOnly(true);
                author.getStyle()
                        .setFontSize("12px")
                        .setOpacity("40%")
                        .setFontWeight(Style.FontWeight.LIGHTER);

                HorizontalLayout hl = new HorizontalLayout();
                hl.add(avatar);
                hl.add(author);

                FormLayout siglePostForm = new FormLayout();
                siglePostForm.add(hl);
                siglePostForm.add(createdDate);
                siglePostForm.add(title);
                siglePostForm.add(content);
                siglePostForm.setResponsiveSteps(
                        // Use one column by default
                        new FormLayout.ResponsiveStep("0", 1),
                        // Use two columns, if the layout's width exceeds 320px
                        new FormLayout.ResponsiveStep("320px", 1),
                        // Use three columns, if the layout's width exceeds 500px
                        new FormLayout.ResponsiveStep("500px", 1));
                siglePostForm.setWidth("80%");
                siglePostForm.getStyle().setBorderRadius("15px");
                siglePostForm.getStyle().setMargin("1px");
                siglePostForm.getStyle().setPadding("10px");

//                siglePostForm.setColspan(title, 2);
//                siglePostForm.setColspan(content, 2);

//                Allow only the owner of post to edit dialog and change
//                If user is not author then redirect
                if (_user.getId() == post.getAuthor().getId()) {
                    siglePostForm.addClickListener(v -> viewPostDialog(post));
                    siglePostForm.getStyle().setBorder("2px solid #636ec2");
                } else{
                    siglePostForm.addClickListener(v -> getUI().ifPresent(ui -> ui.navigate(
                            DetailsView.class,
                            new RouteParameters("postId", post.getId().toString()))));
                    siglePostForm.getStyle().setBorder("2px solid #393E62");
                }

                _postVerticalLayout.add(siglePostForm);

                //Likes and comments
                Button likeBtn = new Button();

                var thumbIcon = LineAwesomeIcon.THUMBS_UP.create();

                AtomicReference<Like> like = new AtomicReference<>(_likeService.getLikeByPostIdAndUserId(_user.getId(), post.getId()));
                if (like.get() == null) {
                    thumbIcon.setColor("gray");
                    likeBtn.setIcon(thumbIcon);
                } else {
                    thumbIcon.setColor("#636ec2");
                    likeBtn.setIcon(thumbIcon);
                }

                likeBtn.addClickListener(l -> {
                    if (like.get() == null) {
                        Like newLike = new Like();
                        newLike.setPost(post);
                        newLike.setIsGivenByUser(_user);
                        newLike.setLike(true);
                        like.set(_likeService.create(newLike));
                        thumbIcon.setColor("#636ec2");
                        likeBtn.setIcon(thumbIcon);
                    } else {
                        boolean isRemoved = _likeService.remove(like.get().getId());
                        if (isRemoved) {
                            like.set(null);
                            thumbIcon.setColor("gray");
                            likeBtn.setIcon(thumbIcon);
                        }
                    }
                });
//                add(likeBtn);
                _postVerticalLayout.add(likeBtn);
            }
        }
    }

    private void viewPostDialog(Post post) {
        ViewPostDialog viewPostDialog = new ViewPostDialog(post, _postService);
        viewPostDialog.open();
        viewPostDialog.addConfirmListener(ll -> createUserPostsList());
    }

    private void createPostDialog() {
        CreatePostDialog createPostDialog = new CreatePostDialog(_postService, _user);
        createPostDialog.open();
        createPostDialog.addConfirmListener(ll -> createUserPostsList());
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