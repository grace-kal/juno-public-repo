package com.master.views.user.admin;

import com.master.models.user.User;
import com.master.services.user.UserService;
import com.master.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@PageTitle("Dashboard of users")
@Route(value = "usersDashboard", layout = MainLayout.class)
@RolesAllowed("Admin")
@Uses(Icon.class)
public class UsersDashboardView extends Div {

    private Grid<User> _grid;
    private Button _editUserButton;
    private UserService _userService;
    private Filters _filters;


    public UsersDashboardView(final UserService userService) {
        setSizeFull();
        addClassNames("gridwith-filters-view");
        this._userService = userService;
        initView();
    }

    public void initView() {
        _filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(/*createMobileFilters(),*/ _filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

//    private HorizontalLayout createMobileFilters() {
//        // Mobile version
//        HorizontalLayout mobileFilters = new HorizontalLayout();
//        mobileFilters.setWidthFull();
//        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
//                LumoUtility.AlignItems.CENTER);
//        mobileFilters.addClassName("mobile-filters");
//
//        Icon mobileIcon = new Icon("lumo", "plus");
//        Span filtersHeading = new Span("Filters");
//        mobileFilters.add(mobileIcon, filtersHeading);
//        mobileFilters.setFlexGrow(1, filtersHeading);
//        mobileFilters.addClickListener(e -> {
//            if (_filters.getClassNames().contains("visible")) {
//                _filters.removeClassName("visible");
//                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
//            } else {
//                _filters.addClassName("visible");
//                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
//            }
//        });
//        return mobileFilters;
//    }

    public static class Filters extends Div implements Specification<User> {

        private final EmailField email = new EmailField("Email");
        private final TextField username = new TextField("Username");
        private final TextField firstName = new TextField("FirstName");
        private final TextField lastName = new TextField("LastName");
        private final IntegerField age = new IntegerField("Age");
        private final RadioButtonGroup<String> roles = new RadioButtonGroup<>("Role");


        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            username.setPlaceholder("Username");
            email.setPlaceholder("Email");
//            firstName.setPlaceholder("First name");
//            lastName.setPlaceholder("Last name");

//            roles.setItems(Roles.User.name(),Roles.Agent.name(),Roles.Admin.name());
//            roles.addClassName("double-width");

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                username.clear();
                email.clear();
                /* roles.clear();*/
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(username, email/*, roles*/, actions);
        }

        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!username.isEmpty()) {
                String lowerCaseFilter = username.getValue().toLowerCase();
                Predicate username = criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                        lowerCaseFilter + "%");
                predicates.add(username);
            }
            if (!age.isEmpty()) {
                String ignore = "- ()";
                Predicate ageMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("age")),
                        "%" + age + "%");
                predicates.add(ageMatch);

            }
            if (!email.isEmpty()) {
                String lowerCaseFilter = email.getValue().toLowerCase();
                Predicate email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        lowerCaseFilter + "%");
                predicates.add(email);
            }
//            if (!roles.isEmpty()) {
//                String lowerCaseFilter = roles.getValue().toLowerCase();
//                Predicate role = criteriaBuilder.like(criteriaBuilder.lower(root.get("role")),
//                        lowerCaseFilter + "%");
//                predicates.add(role);
//            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    private Component createGrid() {
        _grid = new Grid<>(User.class, false);
        _grid.addColumn("firstName").setAutoWidth(true);
        _grid.addColumn("lastName").setAutoWidth(true);
        _grid.addColumn("username").setAutoWidth(true);
        _grid.addColumn("email").setAutoWidth(true);
        _grid.addColumn("age").setAutoWidth(true);
        _grid.addColumn("createdAt").setAutoWidth(true);
        _grid.addColumn("updatedAt").setAutoWidth(true);
        _grid.addColumn("role").setAutoWidth(true);

        _grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, user) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_CONTRAST,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> openUpdateUserDialog(user));
                    button.setIcon(new Icon(VaadinIcon.PENCIL));
                })).setHeader("Edit");

        //edit button on top
//        _editUserButton = new Button("Edit user", l -> openUpdateUserDialog(_grid.asSingleSelect().getValue()));
//        _editUserButton.addThemeVariants(ButtonVariant.LUMO_ICON,
//                            ButtonVariant.LUMO_CONTRAST,
//                            ButtonVariant.LUMO_TERTIARY);
//        _editUserButton.setEnabled(false);
//        add(new HorizontalLayout(_editUserButton));
//        _grid.asSingleSelect().addValueChangeListener(l->{
//            _editUserButton.setEnabled(l.getValue()!=null);
//        });

        _grid.setItems(query -> _userService.getAll(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                _filters).stream());
        _grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        _grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return _grid;
    }

    private void refreshGrid() {
        _grid.getDataProvider().refreshAll();
    }

    private void openUpdateUserDialog(User user) {
        UpdateUserDialog updateUserDialog = new UpdateUserDialog(user, _userService);
        updateUserDialog.addConfirmListener(ll -> refreshGrid());
        updateUserDialog.open();
    }
}
