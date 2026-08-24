package oopProject;

import java.util.ArrayList;

public class TestCentre implements TestCentreTeamManagement {

    private int testCentreNo;
    private String testCentreBuilding;
    private String testCentreAdress;
    private String allocationDate;
    private ArrayList<Invigilator> invigilators = new ArrayList<>();
    private ArrayList<Superintendent> superintendents = new ArrayList<>();
    private Test test;

    public static ArrayList<TestCentre> testCentres = new ArrayList<>();

    public TestCentre() {
        this.testCentreNo = 0;
        this.testCentreBuilding = "";
        this.testCentreAdress = "";
        this.allocationDate = "";
        this.invigilators = new ArrayList<>();
        this.superintendents = new ArrayList<>();
        this.test = new Test();
    }

    public TestCentre(int testCentreNo, String testCentreBuilding, String testCentreAdress, String allocationDate) {
        this.testCentreNo = testCentreNo;
        this.testCentreBuilding = testCentreBuilding;
        this.testCentreAdress = testCentreAdress;
        this.allocationDate = allocationDate;
        this.invigilators = new ArrayList<>();
        this.superintendents = new ArrayList<>();
        this.test = new Test();
    }

    public TestCentre(int testCentreNo, String buildingType, String allocationDate, String n, String fn, String id, String phone, int employeeNo, String city, String des, String supervisor, Double invig_allowance, Double spdt_allowance, int interval, Invigilator[] invs) {
        this.testCentreNo = testCentreNo;
        this.testCentreBuilding = buildingType;
        this.testCentreAdress = city;
        this.allocationDate = allocationDate;
        this.invigilators = new ArrayList<>();
        this.superintendents = new ArrayList<>();
        if (invs != null) {
            for (Invigilator inv : invs) {
                this.invigilators.add(inv);
            }
        }
        Superintendent spd = new Superintendent(n, fn, id, phone, employeeNo, city, invig_allowance, spdt_allowance, interval);
        this.superintendents.add(spd);
    }

    public int getTestCentreNo() {
        return testCentreNo;
    }

    public void setTestCentreNo(int testCentreNo) {
        this.testCentreNo = testCentreNo;
    }

    public String getTestCentreBuilding() {
        return testCentreBuilding;
    }

    public void setTestCentreBuilding(String testCentreBuilding) {
        this.testCentreBuilding = testCentreBuilding;
    }

    public String getTestCentreAdress() {
        return testCentreAdress;
    }

    public void setTestCentreAdress(String testCentreAdress) {
        this.testCentreAdress = testCentreAdress;
    }

    public String getBuildingType() {
        return testCentreBuilding;
    }

    public void setBuildingType(String buildingType) {
        this.testCentreBuilding = buildingType;
    }

    public String getTestCentreCity() {
        return testCentreAdress;
    }

    public void setTestCentreCity(String testCentreCity) {
        this.testCentreAdress = testCentreCity;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String allocationDate) {
        this.allocationDate = allocationDate;
    }

    public ArrayList<Invigilator> getInvigilators() {
        return invigilators;
    }

    public void setInvigilators(ArrayList<Invigilator> invigilators) {
        this.invigilators = invigilators;
    }

    public ArrayList<Superintendent> getSuperintendents() {
        return superintendents;
    }

    public void setSuperintendents(ArrayList<Superintendent> superintendents) {
        this.superintendents = superintendents;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Superintendent getSpd() {
        if (superintendents != null && !superintendents.isEmpty()) {
            return superintendents.get(0);
        }
        return null;
    }

    public void setSpd(Superintendent spd) {
        if (this.superintendents == null) {
            this.superintendents = new ArrayList<>();
        }
        this.superintendents.clear();
        if (spd != null) {
            this.superintendents.add(spd);
        }
    }

    public void setter() {}

    public void update(int no, String building, String adress, String date) {
        this.testCentreNo = no;
        this.testCentreBuilding = building;
        this.testCentreAdress = adress;
        this.allocationDate = date;
    }

    public void update() {}

    public void delete() {
        testCentres.remove(this);
    }

    public void display() {
        System.out.println("Test Centre #: " + testCentreNo + ", Building: " + testCentreBuilding + ", Address: " + testCentreAdress + ", Date: " + allocationDate);
    }

    @Override
    public void addSuperintendent() {}

    public void addSupervisor() {
        addSuperintendent();
    }

    public void showSupervisorDetails() {
        Superintendent spd = getSpd();
        if (spd != null) {
            spd.display();
        }
    }

    @Override
    public void addInvigilators() {}

    public void addInvigilator(Invigilator inv) {
        if (inv != null) {
            invigilators.add(inv);
        }
    }

    public void addSuperintendent(Superintendent spd) {
        if (spd != null) {
            superintendents.add(spd);
        }
    }
}