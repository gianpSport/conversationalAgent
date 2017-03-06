package eu.reply.DM.jUnit;

import org.junit.Rule;
import org.junit.rules.ExternalResource;


public class DM000 {
	@Rule
    public TimerResouces resource = new TimerResouces();
}
