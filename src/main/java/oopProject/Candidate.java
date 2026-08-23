package oopProject;

import java.util.ArrayList;
import java.util.Scanner;

public class Candidate extends Person implements ApplyTest {

    private int formNo;
    private String candidateEmail;
    private String candidatePass;
    private Test test;
    private Boolean status;
    public static ArrayList<Candidate> candidates = new ArrayList<>();
    public static ArrayList<Test> tests = new ArrayList<>();


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public int getFormNo() {
        return formNo;
    }

    public void setFormNo(int formNo) {
        this.formNo = formNo;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<Candidate> candidates) {
        Candidate.candidates = candidates;
    }

    Candidate() {
        super();
        formNo = 0;
        candidateEmail = "";
        candidatePass = "";
        test = new Test();
        status=false;
    }

    Candidate(String n, String fn, String id, String phone, int form, String em, String pass, String testname, int total, int charges, float passing, TestCentre centree,Boolean status) {
        super(n, fn, id, phone);
        formNo = form;
        candidateEmail = em;
        candidatePass = pass;
        this.status=status;
        test = new Test(total, testname, charges, passing, centree);
    }

    @Override
    @SuppressWarnings("resource")
    public void setter() {

        Scanner input = new Scanner(System.in);

        System.out.println("Enter candidate name:");
        name = input.nextLine();

        System.out.println("Enter father name:");
        fname = input.nextLine();


        System.out.println("Enter CNIC(with dahses): ");
        idCard = input.nextLine();

        System.out.println("Enter phone number: ");
        phoneNo = input.nextLine();

        System.out.println("Enter candidate email ");
        candidateEmail = input.next();


        System.out.println("Enter candidate password ");
        candidatePass = input.next();

        while (true) {
            try {
                System.out.println("Enter form number: ");
                formNo = input.nextInt();
                input.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input.Try again.");
                input.nextLine();
            }
        }


    }

    @Override
    public void display() {

        System.out.println();
        System.out.println("Candidate Name: " + name);
        System.out.println("Father name: " + fname);
        System.out.println("Form number: " + formNo);
        System.out.println("Candidate Email: " + candidateEmail);
        System.out.println("Candidate Password: " + candidatePass);
        System.out.println("CNIC: " + idCard);
        System.out.println("Phone number: " + phoneNo);
        System.out.println();
    }

    @Override
    @SuppressWarnings("resource")
    public void update() {

        Scanner input = new Scanner(System.in);

        System.out.println("Enter new candidate name:");
        name = input.nextLine();

        System.out.println("Enter new father name:");
        fname = input.nextLine();


        System.out.println("Enter new CNIC(with dahses): ");
        idCard = input.nextLine();

        System.out.println("Enter new phone number: ");
        phoneNo = input.nextLine();

        System.out.println("Enter new candidate email ");
        candidateEmail = input.next();


        System.out.println("Enter new candidate password ");
        candidatePass = input.next();

        while (true) {
            try {
                System.out.println("Enter new form number: ");
                formNo = input.nextInt();
                input.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("Invalid Input.Try again.");
                input.nextLine();
            }

        }
    }

    @Override
    @SuppressWarnings("resource")
    public void applyTest() {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter your form number: ");
        int formNoToCheck = input.nextInt();
        int formNoFound = 0;

        for (Candidate c : candidates) {

            if (c.getStatus() == true && formNoToCheck==c.getFormNo()) {
                System.out.println("Already applied for test.Cannot apply again");
            } else {
                if (formNoToCheck == c.getFormNo()) {
                    formNoFound++;
                    test.checkAvailableTests();
                    System.out.println();
                    test.setter();
                    tests.add(test);
                    System.out.println("TEST APPLIED SUCCESSFULLY");
                    c.setStatus(true);

                }
            }
        }

        if(formNoFound==0)
        {
            System.out.println("FORM NO DOES NOT EXIST");
        }
    }

    @Override
    @SuppressWarnings("resource")
    public void checkStatus() {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter your form number: ");
        int formNoToCheck = input.nextInt();
        int testApplied = 0;
        for (Candidate c : candidates) {
            if (formNoToCheck == c.getFormNo()) {

                if (c.getStatus() == true) {
                    System.out.println("You are applied for the test");
                    testApplied++;
                    break;
                }


            }

        }

        if (testApplied == 0) {
            System.out.println("No tests applied currently");
            System.out.println("Press 1 to apply for test");
            int booker = input.nextInt();
            if (booker == 1) {
                int record2=0;

                System.out.println("Enter your form number: ");
                int formNoToCheckAndApply = input.nextInt();

                for (Candidate c : candidates) {
                    if (formNoToCheckAndApply == c.getFormNo()) {
                        test.checkAvailableTests();
                        System.out.println();
                        test.setter();
                        tests.add(test);

                        System.out.println("TEST APPLIED SUCCESSFULLY");
                        c.setStatus(true);
                        record2++;
                        break;
                    }
                }
                if(record2==0) {
                    System.out.println("Record does not exist");
                }

            }
        }

        }
    }
