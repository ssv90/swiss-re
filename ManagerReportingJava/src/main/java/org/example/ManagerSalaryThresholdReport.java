package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManagerSalaryThresholdReport {

    private List<ManagerInformation> inSalaryThreshold = new ArrayList<>();
    private List<ManagerInformation> lessSalaryThreshold = new ArrayList<>();
    private List<ManagerInformation> moreSalaryThreshold = new ArrayList<>();

    public List<ManagerInformation> getInSalaryThreshold() {
        return inSalaryThreshold;
    }

    public List<ManagerInformation> getLessSalaryThreshold() {
        return lessSalaryThreshold;
    }

    public List<ManagerInformation> getMoreSalaryThreshold() {
        return moreSalaryThreshold;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ManagerSalaryThresholdReport that = (ManagerSalaryThresholdReport) o;
        return Objects.equals(inSalaryThreshold, that.inSalaryThreshold) && Objects.equals(lessSalaryThreshold, that.lessSalaryThreshold) && Objects.equals(moreSalaryThreshold, that.moreSalaryThreshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inSalaryThreshold, lessSalaryThreshold, moreSalaryThreshold);
    }
}
