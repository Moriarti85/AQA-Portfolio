package tests;

import groovy.util.logging.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import utils.Specifications;

@Slf4j
public class BaseTest {
    protected final static String URL = "https://reqres.in/";
    protected static final Log log = LogFactory.getLog(BaseTest.class);

    @BeforeEach
    public void setUp() {
        setupSpecifications(200);
    }

    protected static void setupSpecifications(int expectedStatusCode) {
        Specifications.resetAllSpecs();
        switch (expectedStatusCode) {
            case 400:
                Specifications.setSpec(
                        Specifications.requestSpecification(URL),
                        Specifications.responseSpecification400()
                );
                break;
            case 403:
                Specifications.setSpec(
                        Specifications.requestSpecification(URL),
                        Specifications.responseSpecification403()
                );
                break;
            default:
                Specifications.setSpec(
                        Specifications.requestSpecification(URL),
                        Specifications.responseSpecification200()
                );
        }
    }
}
