package com.Reporting.Reports;

import com.annotations.CustomFrameworkAnnotations;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import constants.FrameworkConstants;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * ExtentReportImpl class handles the setup, management, and operations related to Extent Reports within the framework.
 */
public final class Report_Extents {

    private static ExtentReports extentReportInstance;
    private static ExtentSparkReporter sparkReporter;
    private static Markup markupText;
    private static final String TEST_INFO_PREFIX = "TEST CASE: - ";

    // Private constructor to prevent instantiation of this utility class
    private Report_Extents() {
    }

    /**
     * Initializes the Extent Report instance and configures the Spark Reporter.
     */
    public static void initializeReport() {
        if (Objects.isNull(extentReportInstance)) {
            extentReportInstance = new ExtentReports();
            sparkReporter = new ExtentSparkReporter(FrameworkConstants.getExtentReportPath() + "TestExecutionReport.html");
            configureSparkReporter();
            extentReportInstance.attachReporter(sparkReporter);
            addSystemInfo();
        }
    }

    /**
     * Configures the Spark Reporter with desired settings like theme, document title, and report name.
     */
    private static void configureSparkReporter() {
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Test Execution Report");
        sparkReporter.config().setReportName("API Testing Report");
    }

    /**
     * Adds system information to the Extent Report such as OS, Java version, etc.
     */
    private static void addSystemInfo() {
        extentReportInstance.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentReportInstance.setSystemInfo("OS Architecture", System.getProperty("os.arch"));
        extentReportInstance.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    /**
     * Starts a new test within the Extent Report.
     *
     * @param testDescription - Descriptive name for the test case.
     * @param testName        - Unique identifier for the test case.
     */
    public static void startTestExecution(String testDescription, String testName) {
        Extent_Report_Manager.setTest(extentReportInstance.createTest(testDescription, testName));
    }

    /**
     * Marks a test case as passed in the Extent Report.
     *
     * @param testName - Name of the test that passed.
     */
    public static void passTest(String testName){
        String passMessage = "<b>" + TEST_INFO_PREFIX + testName + " PASSED" + "</b>";
        markupText = MarkupHelper.createLabel(passMessage, ExtentColor.GREEN);
        Extent_Report_Manager.getTest().pass(markupText);
    }
    /**
     * Marks a test case as skipped in the Extent Report.
     *
     * @param testName - Name of the test that was skipped.
     */
    public static void skipTest(String testName) {

        String skipMessage = "<b>" + TEST_INFO_PREFIX + testName + " SKIPPED" + "</b>";
        markupText = MarkupHelper.createLabel(skipMessage, ExtentColor.ORANGE);
        Extent_Report_Manager.getTest().skip(markupText);
    }
    /**
     * Marks a test case as failed and logs the failure details.
     *
     * @param testName      - Name of the test that failed.
     * @param failureInfo   - Additional information about the failure.
     * @param throwableInfo - Exception details associated with the failure.
     */
    public static void failTest(String testName, String failureInfo, Throwable throwableInfo) {
    	String failMessage = "<b>" + TEST_INFO_PREFIX + testName + " FAILED" + "</b>";
        markupText = MarkupHelper.createLabel(failMessage, ExtentColor.AMBER);
        Extent_Report_Manager.getTest().fail(markupText);
        logStepDetails(failureInfo);
        logErrorDetails(throwableInfo);
    }

    /**
     * Formats a message for test status with appropriate color coding.
     *
     * @param testName - Name of the test case.
     * @param status   - Status of the test (e.g., PASSED, FAILED).
     * @param color    - Color to highlight the status.
     * @return Markup - Markup object with the formatted message.
     */
    private static Markup formatTestStatusMessage(String testName, String status, ExtentColor color) {
        String message = String.format("<b>%s %s %s</b>", TEST_INFO_PREFIX, testName, status);
        return MarkupHelper.createLabel(message, color);
    }

    /**
     * Logs detailed steps of a test case execution in the Extent Report.
     *
     * @param stepDetails - Step-by-step information to log.
     */
    public static void logStepDetails(String stepDetails) {
        Extent_Report_Manager.getTest().info(stepDetails);
    }

    /**
     * Logs exception or error details for failed test cases.
     *
     * @param throwable - Exception object containing the error details.
     */
    public static void logErrorDetails(Throwable throwable) {
        Extent_Report_Manager.getTest().fail(throwable);
    }

    /**
     * Flushes all the data to the Extent Report file and ensures it's saved.
     */
    public static void flushReports() {
        if (extentReportInstance != null) {
            extentReportInstance.flush();
        }
    }

    /**
     * Adds additional test details to the Extent Report by utilizing reflection to gather metadata from the test method.
     *
     * @param method - Method object to extract test annotations and details.
     */
    public static void addDetails(Method method) {
        Extent_Report_Manager.getTest()
                .assignCategory(method.getAnnotation(Test.class).groups())
                .assignCategory(method.getAnnotation(CustomFrameworkAnnotations.class).testCaseType().name());
    }
}
