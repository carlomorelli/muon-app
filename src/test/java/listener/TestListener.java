package listener;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class TestListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            System.out.println("-----------------------------------------------");
            System.out.println("Test: " + testResult.getMethod().getMethodName());
            System.out.println("Duration [ms]: " + (testResult.getEndMillis() - testResult.getStartMillis()));
            System.out.println("Exit status: " + (testResult.getStatus() - 1));
            System.out.println("-----------------------------------------------");
        }
    }

}
