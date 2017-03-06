package eu.reply.DM.jUnit;

import org.junit.rules.ExternalResource;

public class TimerResouces extends ExternalResource{
	TimerResouces() {
    	System.out.println("Timer Init!!");
        DM_AllTests.start=0;
        DM_AllTests.stop=0;
    }

    @Override
    protected void after() {
    	System.out.println("Timer stop!");
    	DM_AllTests.stop=System.currentTimeMillis();
    	System.out.println(DM_AllTests.stop);
    	long timing=DM_AllTests.stop-DM_AllTests.start;
    	System.out.println(timing);
    	DM_AllTests.timer(timing);
    };

    @Override
    protected void before() throws Throwable {
    	System.out.println("Timer start!");
        DM_AllTests.start=System.currentTimeMillis();
        System.out.println(DM_AllTests.start);
    };
}
