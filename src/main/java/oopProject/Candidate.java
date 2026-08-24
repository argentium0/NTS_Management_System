package oopProject;

import java.util.ArrayList;

public class Candidate extends Person implements ApplyTest {

    private int formNo;
    private String candidateEmail;
    private String candidatePass;
    private ArrayList<Test> test = new ArrayList<>();
    private Boolean status;

    public static ArrayList<Candidate> candidates = new ArrayList<>();
    public static ArrayList<Test> tests = new ArrayList<>();

    public Candidate() {
        super();
        this.formNo = 0;
        this.candidateEmail = "";
        this.candidatePass = "";
        this.test = new ArrayList<>();
        this.status = false;
    }

    public Candidate(String n, String fn, String id, String phone, int form, String em, String pass, ArrayList<Test> testList, Boolean status) {
        super(n, fn, id, phone);
        this.formNo = form;
        this.candidateEmail = em;
        this.candidatePass = pass;
        if (testList != null) {
            this.test = testList;
        }
        this.status = status;
    }

    public int getFormNo() {
        return formNo;
    }

    public void setFormNo(int formNo) {
        this.formNo = formNo;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidatePass() {
        return candidatePass;
    }

    public void setCandidatePass(String candidatePass) {
        this.candidatePass = candidatePass;
    }

    public ArrayList<Test> getTest() {
        return test;
    }

    public void setTest(ArrayList<Test> test) {
        this.test = test;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void addBasicData(String n, String fn, String id, String phone, int form, String em, String pass) {
        this.name = n;
        this.fname = fn;
        this.idCard = id;
        this.phoneNo = phone;
        this.formNo = form;
        this.candidateEmail = em;
        this.candidatePass = pass;
    }

    public void updateCandidateInfo(String n, String fn, String id, String phone, int form, String em, String pass) {
        this.name = n;
        this.fname = fn;
        this.idCard = id;
        this.phoneNo = phone;
        this.formNo = form;
        this.candidateEmail = em;
        this.candidatePass = pass;
    }

    public void deleteCandidate() {
        candidates.remove(this);
    }

    @Override
    public void setter() {
        // Base setter stub
    }

    @Override
    public void display() {
        System.out.println("Candidate Name: " + name + ", Email: " + candidateEmail + ", FormNo: " + formNo);
    }

    @Override
    public void update() {
        // General update implementation
    }

    @Override
    public void applyTest() {
        this.status = true;
    }

    @Override
    public void checkStatus() {
        System.out.println("Status: " + (status != null && status ? "Applied" : "Not Applied"));
    }

    public void checkstatus() {
        checkStatus();
    }
}
