package ua.nure.wordle.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ua.nure.wordle.entity.User;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private User user;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
