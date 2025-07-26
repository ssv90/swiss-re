package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

/**
 * BIG COMPANY MANAGER REPORT!
 *
 */
public class ManagerReport
{
    public static final String COMMA_DELIMITER = ",";

    static List<ManagerInformation> allManagersData = new ArrayList<>();

    static HashMap<String, List<ManagerInformation>> directManagerReports = new HashMap<>();

    /**
     * This method is to read the csv file and convert each rows to Manager Information object using BufferedReader
     * Also to create a static list of manager information.
     * @return List of manager information from csv file.
     * @throws IOException
     */
    public List<ManagerInformation> readFromCsv() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(ManagerReport.class.getClassLoader().getResourceAsStream("ManagerData.csv"),
                        StandardCharsets.UTF_8));
        String dataRow;
        System.out.println("Printing the data that is read from csv file");
        while (null != (dataRow = bufferedReader.readLine())) {
            if(!dataRow.equals("Id,firstName,lastName,salary,managerId")) {
                System.out.println(dataRow);
                String[] columnValues = dataRow.split(COMMA_DELIMITER);
                ManagerInformation managerInformation = new ManagerInformation();
                managerInformation.setId(columnValues[0]);
                managerInformation.setFirstName(columnValues[1]);
                managerInformation.setLastName(columnValues[2]);
                managerInformation.setSalary(columnValues[3]);
                managerInformation.setManagerId(columnValues[4]);
                allManagersData.add(managerInformation);
            }
        }
        System.out.println("Total managers in BIG COMPANY " + allManagersData.size());
        return allManagersData;
    }

    /**
     * This method creates a Hash Map to store the manager id as key and list
     * of their direct subordinates as values.
     * @param managerInformations
     * @return HashMap
     */
    public HashMap<String, List<ManagerInformation>> getDirectManagerReports(
            List<ManagerInformation> managerInformations) {

        managerInformations.forEach(manager ->
                directManagerReports.put(manager.getId(), new ArrayList<ManagerInformation>())
        );

        managerInformations.stream()
                .skip(1) // Assuming first entry is the CEO of the company
                .forEach(manager -> {
                    directManagerReports.get(manager.getManagerId()).add(manager);
                });

        System.out.println("Direct Managers View " + directManagerReports);

        return directManagerReports;
    }

    /**
     * Method to recursively identify the manager reporting tree
     * @param managerId
     * @param directManagerReports
     * @param breachThresholdManagers
     * @param counter
     */
    public void getManagerHierarchyCount(String managerId,
                                         HashMap<String, List<ManagerInformation>> directManagerReports,
                                         List<ManagerInformation> breachThresholdManagers, int counter) {
        List<ManagerInformation> subOrdinates = directManagerReports.get(managerId);
        if(null != subOrdinates) {
            for(ManagerInformation subOrdinate : subOrdinates) {
                counter++;
                if (counter > 5) {
                    System.out.println(subOrdinate.getFirstName() + " has more than 4 reports to CE");
                    breachThresholdManagers.add(subOrdinate);
                }
                getManagerHierarchyCount(subOrdinate.getId(), directManagerReports, breachThresholdManagers, counter);
            }
        }
    }

    /**
     * Method to calculate the average salary of all employees who are direct subordinates to their managers.
     * And to identify managers who are earning in threshold, less than threshold and more than threshold.
     * @param directManagerReports
     * @param allManagersData
     * @param thresholdReport
     * @return
     */
    public ManagerSalaryThresholdReport calculateSalaryDifference(HashMap<String, List<ManagerInformation>>
                                                                          directManagerReports,
                                                                  List<ManagerInformation> allManagersData,
                                                                  ManagerSalaryThresholdReport thresholdReport) {
        for(String managerId : directManagerReports.keySet()) {
            List<ManagerInformation> salaryCalList = directManagerReports.get(managerId);
            long totalDirectSubordinatesSalary = 0l;
            double averageDirectSubordinatesSalary = 0;
            for(ManagerInformation mgr : salaryCalList) {
                totalDirectSubordinatesSalary = totalDirectSubordinatesSalary + Long.parseLong(mgr.getSalary());
            }
            if (salaryCalList != null && !salaryCalList.isEmpty()) {
                averageDirectSubordinatesSalary = totalDirectSubordinatesSalary / salaryCalList.size();
            } else {
                averageDirectSubordinatesSalary = totalDirectSubordinatesSalary;
            }

            double minimumThresholdSalary = (0.2 * averageDirectSubordinatesSalary);
            double maximumThresholdSalary = (0.5 * averageDirectSubordinatesSalary);

            for(ManagerInformation allMgr : allManagersData) {
                if(allMgr.getId().equals(managerId)) {
                    int managerSalary = Integer.parseInt(allMgr.getSalary());
                    if(managerSalary > minimumThresholdSalary &&
                            managerSalary <= maximumThresholdSalary) {
                        //System.out.println("Manager who is in the threshold " + allMgr.getFirstName());
                        thresholdReport.getInSalaryThreshold().add(allMgr);
                    } else {
                        //System.out.println("Manager who is in the threshold " + allMgr.getFirstName());
                        StringJoiner managerName = new StringJoiner(" ");
                        managerName.add(allMgr.getFirstName());
                        managerName.add(allMgr.getLastName());
                        if(managerSalary < minimumThresholdSalary) {
                            double salaryDifference = minimumThresholdSalary - managerSalary;
                            System.out.println(managerName.add("is earning less salary than the average of their subordinates by")
                                    .add(String.valueOf(salaryDifference)));
                            thresholdReport.getLessSalaryThreshold().add(allMgr);
                        } else if (managerSalary > maximumThresholdSalary) {
                            double salaryDifference = managerSalary - maximumThresholdSalary;
                            System.out.println(managerName.add("is earning more salary than the average of their subordinates by")
                                    .add(String.valueOf(salaryDifference)));
                            thresholdReport.getMoreSalaryThreshold().add(allMgr);
                        }
                    }
                }
            }
        }
        return thresholdReport;
    }

    public static void main( String[] args )
    {
        System.out.println( "Hello BIG COMPANY!" );

        ManagerReport managerReport = new ManagerReport();

        try {

            //Read data from CSV File
            managerReport.readFromCsv();

            //Get the direct subordinates view for each manager including the CEO
            managerReport.getDirectManagerReports(allManagersData);

            //Calculate the salary difference
            managerReport.calculateSalaryDifference(directManagerReports, allManagersData,
                    new ManagerSalaryThresholdReport());

            //Find the hierarchy of more than 4 managers to CEO
            List<ManagerInformation> breachHierarchyThresholdManagers = new ArrayList<>();
            for(ManagerInformation manager : allManagersData) {
                if(manager.getId().equals(manager.getManagerId())) {
                    List<ManagerInformation> directCeoReports = directManagerReports.get(manager.getManagerId());
                    for(ManagerInformation directCeoReport : directCeoReports) {
                        managerReport.getManagerHierarchyCount(directCeoReport.getId(), directManagerReports,
                                breachHierarchyThresholdManagers, 1);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("IO Exception caught");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Generic Exception caught");
            e.printStackTrace();
        }
    }
}
