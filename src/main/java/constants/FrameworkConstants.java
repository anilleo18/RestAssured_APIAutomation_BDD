package constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.my_enumerations.ExtentReportSpecs;
import com.utils_pack.PropertiesFileImpl;

/**
 * FrameworkConstants class serves as a central repository for common paths and configuration properties.
 */
public class FrameworkConstants {

    // Private constructor to prevent instantiation
    private FrameworkConstants() {
    }

    // Base directory for resource paths
    private static final String RESOURCEPATH = System.getProperty("user.dir");

    // Path to the configuration properties file
    private static final String PROPERTIESFILEPATH = RESOURCEPATH + "/src/test/resources/properties/config.properties";

    // Directory path for storing Extent Reports
    private static final String EXTENTREPORTSPATH = RESOURCEPATH + "/test-reports/";

    // Path to the directory containing JSON files
    private static final String JSONFILESPATH = RESOURCEPATH + "/src/test/resources/jsonfiles";

    // Directory path for storing Rest Assured logs
    private static final String RESTASSUREDLOGSPATH = RESOURCEPATH + "/test-logs/";

    // Default retry count for failed test cases
    public static final int RETRYCOUNTS = 1;

    /**
     * Retrieves the path to the properties file.
     *
     * @return String - Path to the configuration properties file.
     */
    public static String getPropertiesFilePath() {
        return PROPERTIESFILEPATH;
    }

    /**
     * Determines and retrieves the path for Extent Reports, including timestamp if not overridden.
     *
     * @return String - Path to the Extent Reports directory.
     */
    public static String getExtentReportPath() {
        if (PropertiesFileImpl.getDataFromPropertyFile(ExtentReportSpecs.OVERRIDEREPORTS).equalsIgnoreCase("yes")) {
            return EXTENTREPORTSPATH;
        }
        return EXTENTREPORTSPATH + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "/";
    }

    /**
     * Retrieves the configured retry count for failed test cases.
     *
     * @return int - Number of retry attempts.
     */
    public static int getRetryCounts() {
        return RETRYCOUNTS;
    }

    /**
     * Retrieves the path to the directory containing JSON files.
     *
     * @return String - Path to the JSON files directory.
     */
    public static String getJsonFilePath() {
        return JSONFILESPATH;
    }

    /**
     * Retrieves the path to the directory where Rest Assured logs are stored.
     *
     * @return String - Path to the Rest Assured logs directory.
     */
    public static String getRestassuredLogsPath() {
        return RESTASSUREDLOGSPATH;
    }
}
