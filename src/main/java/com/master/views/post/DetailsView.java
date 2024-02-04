package com.master.views.post;

import com.master.models.post.Comment;
import com.master.models.post.Like;
import com.master.models.post.Post;
import com.master.models.user.User;
import com.master.security.AuthenticatedUser;
import com.master.services.post.CommentService;
import com.master.services.post.LikeService;
import com.master.services.post.PostService;
import com.master.views.MainLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Details")
@Route(value = "postdetails/:postId?", layout = MainLayout.class)
@RolesAllowed({"Admin", "User", "Agent"})
public class DetailsView extends VerticalLayout implements BeforeEnterObserver {
    private PostService _postService;
    private LikeService _likeService;
    private CommentService _commentService;
    private User _user;
    private Post _post;

    public DetailsView(final PostService postService, LikeService likeService, CommentService commentService, AuthenticatedUser authenticatedUser) {
        setSizeFull();
        addClassNames("gridwith-filters-view");
        this._postService = postService;
        this._likeService = likeService;
        this._commentService = commentService;
        getAuthenticatedUser(authenticatedUser);
    }

    private void initView() {
        postDetails();
        addCommentField();
        postComments();
    }

    private void postDetails() {
        TextField title = new TextField();
        title.setValue(_post.getTitle());
        title.setReadOnly(true);
        title.getStyle()
                .setFontSize("50px");

        TextArea content = new TextArea();
        content.setValue(_post.getContent());
        content.setReadOnly(true);
        content.getStyle()
                .setFontSize("36px");

        TextField createdDate = new TextField();
        createdDate.setValue(_post.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        createdDate.setReadOnly(true);
        createdDate.getStyle()
                .setFontSize("20px")
                .setOpacity("40%")
                .setFontWeight(Style.FontWeight.LIGHTER)
                .setBorder("0px");

        TextField author = new TextField();
        author.setValue(_post.getAuthor().getUsername());
        author.setReadOnly(true);
        author.getStyle()
                .setFontSize("20px")
                .setOpacity("40%")
                .setFontWeight(Style.FontWeight.LIGHTER);

        User sender = _post.getAuthor();
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

        HorizontalLayout hl = new HorizontalLayout(avatar,author);

        FormLayout siglePostForm = new FormLayout();
        siglePostForm.add(hl);
        siglePostForm.add(createdDate);
        siglePostForm.add(title);
        siglePostForm.add(content);
        siglePostForm.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 2),
                // Use two columns, if the layout's width exceeds 320px
                new FormLayout.ResponsiveStep("320px", 2),
                // Use three columns, if the layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
        siglePostForm.setWidth("90%");
        siglePostForm.getStyle().setBorderRadius("15px");
        siglePostForm.getStyle().setMargin("1px");
        siglePostForm.getStyle().setPadding("10px");

        siglePostForm.setColspan(title, 2);
        siglePostForm.setColspan(content, 2);

//                Allow only the owner of post to edit dialog and change
//                If user is not author then redirect
        if (_user.getId() == _post.getAuthor().getId()) {
            siglePostForm.addClickListener(v -> viewPostDialog(_post));
            siglePostForm.getStyle().setBorder("2px solid #636ec2");
        }

        add(siglePostForm);

        //Likes and comments
        Button likeBtn = new Button();

        var thumbIcon = LineAwesomeIcon.THUMBS_UP.create();

        AtomicReference<Like> like = new AtomicReference<>(_likeService.getLikeByPostIdAndUserId(_user.getId(), _post.getId()));
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
                newLike.setPost(_post);
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
        add(likeBtn);
    }

    private void addCommentField(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        TextField newCommentContent = new TextField();
        newCommentContent.setPlaceholder("Leave a comment");
        Button addCommentBtn = new Button(LineAwesomeIcon.ARROW_RIGHT_SOLID.create());
        addCommentBtn.addClickListener(l->{
            String content = newCommentContent.getValue();
            if(content!=null){
                Comment newComment = new Comment();
                newComment.setContent(content);
                newComment.setPost(_post);
                newComment.setAuthor(_user);
                newComment.setCreatedAt(LocalDateTime.now());
                _commentService.create(newComment);
                newCommentContent.clear();
                postComments();
            }
        });

        horizontalLayout.add(newCommentContent);
        horizontalLayout.add(addCommentBtn);
        add(horizontalLayout);
    }

    private void postComments() {
        List<Comment> comments = _commentService.getCommentsByPostId(_post.getId());
        if(!comments.isEmpty()){
            for (Comment comment:comments) {
                TextArea content = new TextArea();
                content.setValue(comment.getContent());
                content.setReadOnly(true);

                TextField createdDate = new TextField();
                createdDate.setValue(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                createdDate.setReadOnly(true);
                createdDate.getStyle()
                        .setFontSize("12px")
                        .setOpacity("40%")
                        .setFontWeight(Style.FontWeight.LIGHTER);

                TextField author = new TextField();
                author.setValue(comment.getAuthor().getUsername());
                author.setReadOnly(true);
                author.getStyle()
                        .setFontSize("12px")
                        .setOpacity("40%")
                        .setFontWeight(Style.FontWeight.LIGHTER);

                User sender = comment.getAuthor();
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

                HorizontalLayout hl = new HorizontalLayout(avatar,author);

                FormLayout sigleCommentForm = new FormLayout();
                sigleCommentForm.add(hl);
                sigleCommentForm.add(createdDate);
                sigleCommentForm.add(content);
                sigleCommentForm.setResponsiveSteps(
                        // Use one column by default
                        new FormLayout.ResponsiveStep("0", 2),
                        // Use two columns, if the layout's width exceeds 320px
                        new FormLayout.ResponsiveStep("320px", 2),
                        // Use three columns, if the layout's width exceeds 500px
                        new FormLayout.ResponsiveStep("500px", 2));
                sigleCommentForm.setWidth("80%");
                sigleCommentForm.getStyle().setBorderRadius("15px");
                sigleCommentForm.getStyle().setMargin("1px");
                sigleCommentForm.getStyle().setPadding("10px");
                sigleCommentForm.setColspan(content, 2);
                add(sigleCommentForm);

//                Likes for comments
                Button likeBtnComments = new Button();

                var thumbIcon = LineAwesomeIcon.THUMBS_UP.create();

                AtomicReference<Like> like = new AtomicReference<>(_likeService.getLikeByCommentIdAndUserId(_user.getId(), comment.getId()));
                if (like.get() == null) {
                    thumbIcon.setColor("gray");
                    likeBtnComments.setIcon(thumbIcon);
                } else {
                    thumbIcon.setColor("#636ec2");
                    likeBtnComments.setIcon(thumbIcon);
                }

                likeBtnComments.addClickListener(l -> {
                    if (like.get() == null) {
                        Like newLike = new Like();
                        newLike.setComment(comment);
                        newLike.setIsGivenByUser(_user);
                        newLike.setLike(true);
                        like.set(_likeService.create(newLike));
                        thumbIcon.setColor("#636ec2");
                        likeBtnComments.setIcon(thumbIcon);
                    } else {
                        boolean isRemoved = _likeService.remove(like.get().getId());
                        if (isRemoved) {
                            like.set(null);
                            thumbIcon.setColor("gray");
                            likeBtnComments.setIcon(thumbIcon);
                        }
                    }
                });
//                add(likeBtn);
                add(likeBtnComments);
            }
        }
    }

    private void viewPostDialog(Post post) {
        ViewPostDialog viewPostDialog = new ViewPostDialog(post, _postService);
        viewPostDialog.open();
        viewPostDialog.addConfirmListener(ll -> initView());
    }

    private void getAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            _user = maybeUser.get();
        }
        // redirect
        getUI().ifPresent(u -> u.navigate("login"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> postId = event.getRouteParameters().get("postId");
        postId.ifPresent(s -> _post = _postService.getById(Long.parseLong(s)));
        initView();
    }
}
