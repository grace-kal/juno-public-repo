package com.master.views.forth;

import com.master.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Forth")
@Route(value = "forth", layout = MainLayout.class)
@RolesAllowed("User")
@Uses(Icon.class)
public class ForthView extends Composite<VerticalLayout> {

    public ForthView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
    }
}
