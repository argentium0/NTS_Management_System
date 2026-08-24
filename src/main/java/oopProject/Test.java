package oopProject;

import java.util.ArrayList;

public class Test implements TestManagement {

    private int testID;
    private String testName;
    private int marks;
    private double charges;
    private float passingPer;

    public static ArrayList<Test> testList = new ArrayList<>();

    public Test() {
        this.testID = 0;
        this.testName = "";
        this.marks = 0;
        this.charges = 0.0;
        this.passingPer = 0.0f;
    }

    public Test(int testID, String testName, int marks, double charges, float passingPer) {
        this.testID = testID;
        this.testName = testName;
        this.marks = marks;
        this.charges = charges;
        this.passingPer = passingPer;
    }

    public Test(int marks, String testName, int charges, float passingPer, TestCentre centre) {
        this.testID = (int) (Math.random() * 1000);
        this.testName = testName;
        this.marks = marks;
        this.charges = charges;
        this.passingPer = passingPer;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getTestId() {
        return testID;
    }

    public void setTestId(int testId) {
        this.testID = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public float getPassingPer() {
        return passingPer;
    }

    public void setPassingPer(float passingPer) {
        this.passingPer = passingPer;
    }

    public float getPassing_percentage() {
        return passingPer;
    }

    public void setPassing_percentage(float passing_percentage) {
        this.passingPer = passing_percentage;
    }

    public void setter() {}

    public void update(String name, int marks, double charges, float passingPer) {
        updateTestInfo(name, marks, charges, passingPer);
    }

    public void update() {}

    public void updateTestInfo(String name, int marks, double charges, float passingPer) {
        this.testName = name;
        this.marks = marks;
        this.charges = charges;
        this.passingPer = passingPer;
    }

    public void deleteTestInfo() {
        testList.remove(this);
    }

    public void display() {
        displayTestInfo();
    }

    public void displayTestInfo() {
        System.out.println("Test ID: " + testID + ", Name: " + testName + ", Marks: " + marks + ", Charges: " + charges + ", Passing %: " + passingPer);
    }

    @Override
    public void checkTestDetails() {
        displayTestInfo();
    }

    public void checkAvailableTests() {
        checkTestDetails();
    }
}
