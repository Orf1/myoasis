package dev.orf1.clevergreen.views.support;

import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.orf1.clevergreen.security.AuthenticatedUser;
import dev.orf1.clevergreen.views.MainLayout;
import java.util.UUID;
import javax.annotation.security.PermitAll;

@PageTitle("Support")
@Route(value = "support", layout = MainLayout.class)
@PermitAll
public class SupportView extends VerticalLayout {

    public SupportView(AuthenticatedUser authenticatedUser) {
        addClassName("support-view");
        setSpacing(false);
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), authenticatedUser.get().get().getName());
        Tabs tabs = new Tabs(new Tab("#oasis-support"));
        tabs.setWidthFull();
        CollaborationMessageList list = new CollaborationMessageList(userInfo, "chat/#oasis-support");
        list.setWidthFull();
        list.addClassNames("chat-view-message-list");
        CollaborationMessageInput input = new CollaborationMessageInput(list);
        input.addClassNames("chat-view-message-input");
        input.setWidthFull();
        add(tabs, list, input);
        setSizeFull();
        expand(list);
        tabs.addSelectedChangeListener(event -> {
            String channelName = event.getSelectedTab().getLabel();
            list.setTopic("chat/" + channelName);
        });
    }

}