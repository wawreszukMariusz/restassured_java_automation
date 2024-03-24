package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Setup implements ITestListener {

    private static ExtentReports extentReports;
    private static ExtentTest test;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    public static String name = "amod";

    public void onStart(ITestContext context) {
        String fileName = ExtentReportManager.getGetReportNameWithTimeStamp();
        String fullReportPath = System.getProperty("user.dir") + "\\reports\\" + fileName;
        extentReports = ExtentReportManager.createInstance(fullReportPath, "OpenAI API automation", "Report of test execution");
    }

    public void onTestSuccess(ITestResult result) {
        test.pass("Test passed");
    }

    public void onTestFailure(ITestResult result) {
        test.fail("Test failed");
    }

    public void onTestSkipped(ITestResult result) {
        test.skip("Test skipped");
    }

    public void onFinish(ITestContext context) {
        if(extentReports != null) {
            extentReports.flush();
        }
    }

    public void onTestStart(ITestResult result) {
       test = extentReports.createTest("Test Name " + result.getTestClass().getName() + " - " + result.getMethod().getMethodName());
       extentTest.set(test);
    }
}
