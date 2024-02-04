package com.master.views.user;

import com.master.models.post.Like;
import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.post.LikeService;
import com.master.services.post.PostService;
import com.master.services.user.UserService;
import com.master.views.MainLayout;
import com.master.views.post.ViewPostDialog;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@AnonymousAllowed
@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {
    private UserService _userService;
    private LikeService _likeService;
    private AuthenticatedUser _authenticatedUser;
    private User _user;
    private PostService _postService;
    private List<Post> _postList;
    private VerticalLayout _layout;
    private String _avatarPath;


    public ProfileView(final UserService userService, final PostService postService, final LikeService likeService, AuthenticatedUser authenticatedUser) {
        setSizeFull();
        this._userService = userService;
        this._postService = postService;
        this._likeService = likeService;
        getAuthenticatedUser(authenticatedUser);
//        this._authenticatedUser = authenticatedUser;
        initView();
    }

    private void initView() {
//        HorizontalLayout topLayout = new HorizontalLayout(/*createUserQuizList(),*/ createUserInfoDiv());
//        topLayout.setSizeFull();
//        topLayout.setPadding(false);
//        topLayout.setSpacing(false);
        createUserInfoDiv();

        HorizontalLayout bottomLayout = new HorizontalLayout(createUserPostsList());
        bottomLayout.setSizeFull();
        bottomLayout.setPadding(false);
        bottomLayout.setSpacing(false);
        add(bottomLayout);
    }

    //    private VerticalLayout createUserQuizList() {
//    }
//
    private void createUserInfoDiv() {
        TextField username = new TextField();
        username.setValue(_user.getUsername());
        username.setReadOnly(true);
        username.getStyle().setFontSize("20px");

        TextField fname = new TextField();
        String fnameFromDb = _user.getFirstName();
        if (fnameFromDb != null) {
            fname.setValue(_user.getFirstName());
        }
        fname.setReadOnly(true);
        fname.getStyle().setFontSize("15px");

        TextField lname = new TextField();
        String lnameFromDb = _user.getLastName();
        if (lnameFromDb != null) {
            lname.setValue(_user.getLastName());
        }
        lname.setReadOnly(true);
        lname.getStyle().setFontSize("15px");

        TextField age = new TextField();
        age.setValue(String.valueOf(_user.getAge()));
        age.setReadOnly(true);
        age.getStyle().setFontSize("15px");

        TextField email = new TextField();
        email.setValue(_user.getEmail());
        email.setReadOnly(true);
        email.getStyle().setFontSize("15px");

        Avatar avatar = new Avatar(_user.getUsername());
        if (_user.getProfilePicture() != null) {
            avatar.setImage("images/profilepictures/" + _user.getProfilePicture());
            _avatarPath = _user.getProfilePicture();
        } else {
            int colorIndex = new Random().nextInt(2, 20);
            if (_user.getColorIndex() == 0) _user.setColorIndex(colorIndex);
            else colorIndex = _user.getColorIndex();
            avatar.setColorIndex(colorIndex);
        }
        avatar.setThemeName("large");
        avatar.getElement().setAttribute("tabindex", "1");

        add(avatar);
//        Button selectAvatartBtn = new Button("Edit avatar");
//        selectAvatartBtn.addClickListener(l -> {
//            Div profilepics = new Div();
//
//        });
//        add(selectAvatartBtn);

        FormLayout formLayout = new FormLayout(username, fname, lname, email, age);
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

        formLayout.setColspan(username, 2);

        formLayout.addClickListener(v -> editProfileDialog());

        add(formLayout);
    }

    private void editProfileDialog() {
        EditUserDialog editUserDialog = new EditUserDialog(_user, _userService);
        editUserDialog.open();
        editUserDialog.addConfirmListener(ll -> initView());
    }

    private VerticalLayout createUserPostsList() {
        _layout = new VerticalLayout();
        _postList = _postService.getAllPostsByUserId(_user.getId());
        if (!_postList.isEmpty()) {
            for (Post post : _postList) {
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

                TextField author = new TextField();
                author.setValue(post.getAuthor().getUsername());
                author.setReadOnly(true);
                author.getStyle()
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

                HorizontalLayout hl = new HorizontalLayout(avatar, author);

                FormLayout siglePostForm = new FormLayout();
                siglePostForm.add(hl);
                siglePostForm.add(createdDate);
                siglePostForm.add(title);
                siglePostForm.add(content);
                siglePostForm.setResponsiveSteps(
                        // Use one column by default
                        new ResponsiveStep("0", 2),
                        // Use two columns, if the layout's width exceeds 320px
                        new ResponsiveStep("320px", 2),
                        // Use three columns, if the layout's width exceeds 500px
                        new ResponsiveStep("500px", 2));
                siglePostForm.setWidth("40%");
                siglePostForm.getStyle().setBorder("2px solid #393E62");
                siglePostForm.getStyle().setBorderRadius("15px");
                siglePostForm.getStyle().setMargin("1px");
                siglePostForm.getStyle().setPadding("10px");
                siglePostForm.setColspan(title, 2);
                siglePostForm.setColspan(content, 2);
                siglePostForm.getStyle().setBorder("2px solid #636ec2");
                siglePostForm.addClickListener(v -> viewPostDialog(post));
                _layout.add(siglePostForm);

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
                _layout.add(likeBtn);
            }
            return _layout;
        }
        return _layout;
    }

    private void viewPostDialog(Post post) {
        ViewPostDialog viewPostDialog = new ViewPostDialog(post, _postService);
        viewPostDialog.open();
        viewPostDialog.addConfirmListener(ll -> createUserPostsList());
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
