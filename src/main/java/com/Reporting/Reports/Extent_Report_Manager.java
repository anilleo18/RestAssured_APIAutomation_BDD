package com.Reporting.Reports;

import com.aventstack.extentreports.ExtentTest;

/**
 * Extent_Report_Manager class to ensure Thread Safety for Extent Report Instances during parallel test execution.
 */
public class Extent_Report_Manager {

    private Extent_Report_Manager() {
        // Private constructor to prevent instantiation
    }

    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    /**
     * Associates the current thread with a specific ExtentTest instance.
     *
     * @param testInstance - The ExtentTest instance to be associated with the current thread.
     */
    static void setTest(ExtentTest testInstance) {
        testThreadLocal.set(testInstance);
    }

    /**
     * Retrieves the ExtentTest instance associated with the current thread.
     *
     * @return The ExtentTest instance for the current thread.
     */
    static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    /**
     * Removes the ExtentTest instance associated with the current thread, ensuring no memory leaks.
     */
    static void removeTest() {
        testThreadLocal.remove();
    }
}
