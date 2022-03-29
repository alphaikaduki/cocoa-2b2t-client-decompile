package me.alpha432.oyvey.event.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface CommitEvent {

    EventPriority priority() default EventPriority.NORMAL;
}
