package com.master.views.third;

import com.master.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Third")
@Route(value = "third", layout = MainLayout.class)
@RolesAllowed("Admin")
@Uses(Icon.class)
public class ThirdView extends Composite<VerticalLayout> {

    public ThirdView() {
        getContent().setHeightFull();
        getContent().setWidthFull();
    }
}
