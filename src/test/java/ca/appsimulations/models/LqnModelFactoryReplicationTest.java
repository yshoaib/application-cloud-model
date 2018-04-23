package ca.appsimulations.models;

import ca.appsimulations.jlqninterface.lqn.model.LqnModel;
import ca.appsimulations.jlqninterface.lqn.model.LqnXmlDetails;
import ca.appsimulations.jlqninterface.lqn.model.SolverParams;
import ca.appsimulations.jlqninterface.lqn.model.handler.LqnSolver;
import ca.appsimulations.jlqninterface.lqn.model.writer.LqnModelWriter;
import ca.appsimulations.models.model.application.App;
import ca.appsimulations.models.model.application.DefaultAppBuilder;
import ca.appsimulations.models.model.cloud.MultipleInstanceContainerImageCloudBuilder;
import ca.appsimulations.models.model.cloud.MultipleInstanceContainerImageCloudBuilderWithReplication;
import ca.appsimulations.models.model.lqnmodel.LqnModelFactory;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.xmlunit.validation.ValidationResult;
import org.xmlunit.validation.Validator;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LqnModelFactoryReplicationTest {


    private File outputFile;
    private SoftAssertions softly;
    private final String LQN_XSD = "lqn.xsd";
    private final String INPUT_LQNX = "input3_rep.lqnx";
    private final String OUTPUT_FILE = "test-output.lqnx";

    private static final double CONVERGENCE = 0.01;
    private static final int ITERATION_LIMIT = 50_000;
    private static final double UNDER_RELAX_COEFF = 0.9;
    private static final int PRINT_INTERVAL = 1;
    private static final String XML_NS_URL = "http://www.w3.org/2001/XMLSchema-instance";
    private static final String SCHEMA_LOCATION = "lqn.xsd";
    private static final String XML_NAME = "input-rep";
    private static final String XML_DESCRIPTION = "lqn2xml 5.4 solution for model from: input-rep.lqn.";
    private static final String COMMENT = "Test System - 10 users think 2 - no replication";

    @BeforeMethod
    void beforeMethod() {
        outputFile = new File(OUTPUT_FILE);
        outputFile.delete();
        softly = new SoftAssertions();
    }

    @Test(enabled = false)
    public void testLqnModelFactory() throws Exception {
        String appName = "test";
        int maxReplicas = 10;
        double responseTime = 350.0;
        App app = DefaultAppBuilder.build(appName,
                                          maxReplicas,
                                          responseTime);

        MultipleInstanceContainerImageCloudBuilderWithReplication.build(app);

        LqnXmlDetails xmlDetails = buildLqnXmlDetails();
        SolverParams solverParams = buildSolverParams();
        LqnModel lqnModel = LqnModelFactory.buildWithLqnReplication(app, xmlDetails, solverParams);
        new LqnModelWriter().write(lqnModel, outputFile.getAbsolutePath());
        //        assertXmlAgainstSchema(outputFile.getAbsolutePath(), LQN_XSD);
        assertThat(readFile(outputFile)).as("xml contents verification").isXmlEqualToContentOf(getResourceFile(
                INPUT_LQNX));
        softly.assertAll();
    }

    @Test(enabled = false)
    public void testLqnModelFactory2() throws Exception {
        String appName = "test";
        int maxReplicas = 10;
        double responseTime = 350.0;
        App app = DefaultAppBuilder.build(appName,
                                          maxReplicas,
                                          responseTime);

        MultipleInstanceContainerImageCloudBuilder.build(app);

        LqnXmlDetails xmlDetails = buildLqnXmlDetails();
        SolverParams solverParams = buildSolverParams();
        LqnModel lqnModel = LqnModelFactory.buildWithLqnReplication(app, xmlDetails, solverParams);
        new LqnModelWriter().write(lqnModel, outputFile.getAbsolutePath());
        //     assertXmlAgainstSchema(outputFile.getAbsolutePath(), LQN_XSD);
        assertThat(readFile(outputFile)).as("xml contents verification").isXmlEqualToContentOf(getResourceFile(
                INPUT_LQNX));
        softly.assertAll();
    }

    private SolverParams buildSolverParams() {
        return SolverParams
                .builder()
                .comment(COMMENT)
                .convergence(CONVERGENCE)
                .iterationLimit(ITERATION_LIMIT)
                .underRelaxCoeff(UNDER_RELAX_COEFF)
                .printInterval(PRINT_INTERVAL)
                .build();
    }

    private LqnXmlDetails buildLqnXmlDetails() {
        return LqnXmlDetails
                .builder()
                .name(XML_NAME)
                .xmlnsXsi(XML_NS_URL)
                .description(XML_DESCRIPTION)
                .schemaLocation(SCHEMA_LOCATION)
                .build();
    }


    private String readFile(File file) throws IOException {
        return IOUtils.toString(file.toURI(), "UTF-8");
    }

    private void assertXmlAgainstSchema(String xmlFile, String schemaPath) throws Exception {
        Validator v = Validator.forLanguage(org.xmlunit.validation.Languages.W3C_XML_SCHEMA_NS_URI);
        v.setSchemaSource(new StreamSource(getResourceFile(schemaPath)));
        ValidationResult validationResult = v.validateInstance(new StreamSource(xmlFile));
        softly.assertThat(validationResult.isValid()).as("xml valid against schema").isTrue();
    }

    public String readResource(String resourceName) throws Exception {
        return IOUtils.toString(
                this.getClass().getClassLoader().getResourceAsStream(resourceName),
                "UTF-8");
    }

    public File getResourceFile(String resourceName) throws Exception {
        return new File(this.getClass().getClassLoader().getResource(resourceName).toURI());
    }

}
