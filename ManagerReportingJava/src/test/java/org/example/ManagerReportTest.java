package org.example;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Unit test for BIG COMPANY MANAGER REPORT!
 */
public class ManagerReportTest  {

    private ManagerReport managerReport;

    @Test
    public void testReadFromCSVFileSuccess() throws IOException {
        managerReport = new ManagerReport();
        List<ManagerInformation> managers = managerReport.readFromCsv();
        Assert.assertNotNull(managers);
        Assert.assertEquals(13, managers.size());
    }

    @Test
    public void testGetManagersSuccess() throws IOException {
        managerReport = new ManagerReport();
        HashMap<String, List<ManagerInformation>> directManagers = managerReport.getDirectManagerReports(populateStubManagerInformation());
        Assert.assertNotNull(directManagers);
        Assert.assertEquals(11, directManagers.keySet().size());
    }

    @Test
    public void testGetManagerHavingMoreThan4Success() throws IOException {
        managerReport = new ManagerReport();
        HashMap<String, List<ManagerInformation>> directManagers = managerReport.getDirectManagerReports(populateStubManagerInformation());
        List<ManagerInformation> breachThresholdManagers = new ArrayList<>();
        managerReport.getManagerHierarchyCount("1", directManagers, breachThresholdManagers, 1);
        Assert.assertNotNull(breachThresholdManagers);
        Assert.assertEquals(4, breachThresholdManagers.size());
    }

    @Test
    public void testGetManagerHavingMoreThan4WhenInvalidManagerIdIsPassed() throws IOException {
        managerReport = new ManagerReport();
        HashMap<String, List<ManagerInformation>> directManagers = managerReport.getDirectManagerReports(populateStubManagerInformation());
        List<ManagerInformation> breachThresholdManagers = new ArrayList<>();
        managerReport.getManagerHierarchyCount("9999", directManagers, breachThresholdManagers, 1);
        Assert.assertNotNull(breachThresholdManagers);
        Assert.assertTrue(breachThresholdManagers.isEmpty());
    }

    @Test
    public void testGetManagerInThresholdSalary() throws IOException {
        managerReport = new ManagerReport();
        HashMap<String, List<ManagerInformation>> directManagers = managerReport.getDirectManagerReports(populateStubManagerInformation());

        ManagerSalaryThresholdReport thresholdReport = new ManagerSalaryThresholdReport();
        managerReport.calculateSalaryDifference(directManagers, populateStubManagerInformation(), thresholdReport);

        Assert.assertNotNull(thresholdReport);
        Assert.assertFalse(thresholdReport.getInSalaryThreshold().isEmpty());
    }

    @Test
    public void testGetManagerLessThanThresholdSalary() throws IOException {
        managerReport = new ManagerReport();
        HashMap<String, List<ManagerInformation>> directManagers = managerReport.getDirectManagerReports(populateStubManagerInformation());

        ManagerSalaryThresholdReport thresholdReport = new ManagerSalaryThresholdReport();
        managerReport.calculateSalaryDifference(directManagers, populateStubManagerInformation(), thresholdReport);

        Assert.assertNotNull(thresholdReport);
        Assert.assertFalse(thresholdReport.getLessSalaryThreshold().isEmpty());
    }

    @Test
    public void testGetManagerMoreThresholdSalary() throws IOException {
        managerReport = new ManagerReport();
        HashMap<String, List<ManagerInformation>> directManagers = managerReport.getDirectManagerReports(populateStubManagerInformation());

        ManagerSalaryThresholdReport thresholdReport = new ManagerSalaryThresholdReport();
        managerReport.calculateSalaryDifference(directManagers, populateStubManagerInformation(), thresholdReport);

        Assert.assertNotNull(thresholdReport);
        Assert.assertFalse(thresholdReport.getMoreSalaryThreshold().isEmpty());
    }

    private List<ManagerInformation> populateStubManagerInformation() {
        List<ManagerInformation> managerInformations = new ArrayList<>();
        managerInformations.add(new ManagerInformation("1", "Alice", "Smith", "10000", "1"));
        managerInformations.add(new ManagerInformation("2", "Bob", "Jones", "1000", "1"));
        managerInformations.add(new ManagerInformation("3", "Carol", "Lee", "7000", "1"));
        managerInformations.add(new ManagerInformation("4", "Dan", "Wong", "7500", "2"));
        managerInformations.add(new ManagerInformation("5", "Eva", "Kim", "7200", "2"));
        managerInformations.add(new ManagerInformation("6", "Frank", "Ng", "7100", "3"));
        managerInformations.add(new ManagerInformation("7", "Meg", "Fox", "11100", "6"));
        managerInformations.add(new ManagerInformation("8", "Danny", "Men", "7100", "7"));
        managerInformations.add(new ManagerInformation("9", "Ankit", "Sen", "5500", "8"));
        managerInformations.add(new ManagerInformation("10", "Emily", "Paris", "12000", "9"));
        managerInformations.add(new ManagerInformation("11", "Arjun", "Karan", "12000", "9"));
        return managerInformations;
    }

}
