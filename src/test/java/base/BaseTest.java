package base;

import listeners.RetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.Reporting.Reports.Report_Extents;

import java.lang.reflect.Method;

public class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {

        // Extent Report Initialization
        Report_Extents.initializeReport();

        //Initializing Tests with Retry Analyzer Annotation
        for (ITestNGMethod method : context.getAllTestMethods()) {
            method.setRetryAnalyzerClass(RetryAnalyzer.class);
        }
    }

    @BeforeMethod(alwaysRun = true)
    protected void setUp(Method method) {

        // Extent Report Initialization
        String testDescription = method.getAnnotation(Test.class).testName();
        String testName = method.getName();
        Report_Extents.startTestExecution(testDescription, testName);
        Report_Extents.logStepDetails(testName + " -> Execution starts");

    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown(ITestResult result, Method method) {
        String testName = result.getName();
        Report_Extents.logStepDetails(result.getName() + " -> Execution ended");
        Report_Extents.addDetails(method);

        if (ITestResult.FAILURE == result.getStatus()) {
            RetryAnalyzer rerun = new RetryAnalyzer();
            rerun.retry(result);
            Report_Extents.failTest(testName, result.getThrowable().getMessage(), result.getThrowable());

        } else if (ITestResult.SUCCESS == result.getStatus()) {
            Report_Extents.passTest(testName);

        } else if (ITestResult.SKIP == result.getStatus()) {
            Report_Extents.skipTest(testName);
        }

    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        Report_Extents.flushReports();
    }
}
