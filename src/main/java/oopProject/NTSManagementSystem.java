package oopProject;

import java.util.ArrayList;
import java.util.Scanner;

public class NTSManagementSystem {

    @SuppressWarnings({"resource", "enhanced-switch", "fallthrough", "preview"})
    public static void main(String[] args) {

        int choiceAtMainMenu=1;
        int choiceAtCandidateMenu=0;
        int choiceAtEmployeeMenu=0;
        int choiceAtCentresMenu=0;
        int choiceAtTestMenu=0;

        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<TestCentre> testCentres = new ArrayList<>();

        Candidate candidate = new Candidate();
        Employee employee = new Employee();
        TestCentre testCentre = new TestCentre();
        Test test = new Test();

        while (choiceAtMainMenu!=0) {
            System.out.println();
            System.out.println("\t\t\tWELCOME TO THE NTS MANAGEMENT SYSTEM");
            System.out.println();
            System.out.println("1.CANDIDATE PORTAL");
            System.out.println("2.EMPLOYEE PORTAL");
            System.out.println("3.TEST CENTRES PORTAL");
            System.out.println("4.TESTS PORTAL");
            System.out.println("0.EXIT");

            while (true) {
                Scanner input = new Scanner(System.in);
                try {
                    System.out.println("Enter your choice: ");
                    input = new Scanner(System.in);
                    choiceAtMainMenu = input.nextInt();
                    input.nextLine();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input.Try again.");
                    input.nextLine();
                }
            }

            switch (choiceAtMainMenu) {



                    case 1: {

                        while(choiceAtCandidateMenu!=8) {

                        System.out.println();
                        System.out.println("\t\t\tWELCOME TO THE NTS CANDIDATE PORTAL");
                        System.out.println();
                        System.out.println("1.ADD CANDIDATE DATA");
                        System.out.println("2.SEARCH CANDIDATE DATA");
                        System.out.println("3.UPDATE CANDIDATE DATA");
                        System.out.println("4.DISPLAY CANDIDATE DATA");
                        System.out.println("5.DELETE CANDIDATE DATA");
                        System.out.println("6.APPLY FOR TEST");
                        System.out.println("7.SHOW APPLICATION STATUS");
                        System.out.println("8.EXIT");

                        while (true) {
                            Scanner input = new Scanner(System.in);
                            try {
                                System.out.println("Enter your choice: ");
                                input = new Scanner(System.in);
                                choiceAtCandidateMenu = input.nextInt();
                                input.nextLine();
                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid Input.Try again.");
                                input.nextLine();
                            }
                        }

                        switch (choiceAtCandidateMenu) {
                            case 1: {
                                Candidate candidate1 = new Candidate();
                                candidate1.setter();
                                Candidate.candidates.add(candidate1);
                                System.out.println("ADDED SUCCESSFULLY");
                                System.out.println();
                            }
                            break;

                            case 2: {
                                System.out.println("Enter form number of candidate to search: ");
                                Scanner input = new Scanner(System.in);
                                int candidateSearch = input.nextInt();

                                for (Candidate candidateToSearch : Candidate.candidates) {
                                    if (candidateSearch == candidateToSearch.getFormNo()) {
                                        candidateToSearch.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 3: {

                                System.out.println("Enter form number of candidate to Update: ");
                                Scanner input = new Scanner(System.in);
                                int candidateUpdate = input.nextInt();

                                for (Candidate candidateToUpdate : Candidate.candidates) {
                                    if (candidateUpdate == candidateToUpdate.getFormNo()) {
                                        candidateToUpdate.update();
                                    }
                                }

                            }
                            break;

                            case 4: {

                                if (Candidate.candidates.isEmpty()) {
                                    System.out.println("NO RECORDS AVAILABLE");
                                }
                                else {
                                for (Candidate CandidatesToDisplay : Candidate.candidates) {
                                        System.out.printf("CANDIDATE %d", (Candidate.candidates.indexOf(CandidatesToDisplay) + 1));
                                        CandidatesToDisplay.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 5: {
                                System.out.println("Enter form number of candidate to Delete: ");
                                Scanner input = new Scanner(System.in);
                                int candidateDelete = input.nextInt();
                                boolean candidateFound = false;

                                for (Candidate candidateToDelete : Candidate.candidates) {
                                    if (candidateDelete == candidateToDelete.getFormNo()) {
                                        Candidate.candidates.remove(candidateToDelete);
                                        System.out.println("Record removed successfully");
                                        candidateFound = true;
                                        System.out.println();
                                        break;
                                    }

                                }
                                if (!candidateFound) {
                                    System.out.println("Record Not Found");
                                }

                            }
                            break;

                            case 6: {

                                candidate.applyTest();

                            }
                            break;

                            case 7: {

                                candidate.checkStatus();

                                }
                            break;

                            case 8:
                                break;

                            default:
                                System.out.println("Inavlid choice");
                        }


                    }
                    break;
                }

                case 2: {

                    while (choiceAtEmployeeMenu != 7) {

                        System.out.println();
                        System.out.println("\t\t\tWELCOME TO THE NTS EMPLOYEE PORTAL");
                        System.out.println();
                        System.out.println("1.ADD EMPLOYEE DATA");
                        System.out.println("2.SEARCH EMPLOYEE DATA");
                        System.out.println("3.UPDATE EMPLOYEE DATA");
                        System.out.println("4.DISPLAY EMPLOYEE DATA");
                        System.out.println("5.DELETE EMPLOYEE DATA");
                        System.out.println("6.ALLOWANCE MANAGEMENT");
                        System.out.println("7.EXIT");

                        while (true) {
                            Scanner input = new Scanner(System.in);
                            try {
                                System.out.println("Enter your choice: ");
                                input = new Scanner(System.in);
                                choiceAtEmployeeMenu = input.nextInt();
                                input.nextLine();
                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid Input.Try again.");
                                input.nextLine();
                            }
                        }

                        switch (choiceAtEmployeeMenu) {
                            case 1: {
                                Employee employee1 = new Employee();
                                employee1.setter();
                                employees.add(employee1);
                                System.out.println("ADDED SUCCESSFULLY");
                                System.out.println();
                            }
                            break;

                            case 2: {
                                System.out.println("Enter ID number of employee to search: ");
                                Scanner input = new Scanner(System.in);
                                int employeeSearch = input.nextInt();

                                for (Employee employeeToSearch : employees) {
                                    if (employeeSearch == employeeToSearch.getEmployeeNo()) {
                                        employeeToSearch.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 3: {
                                System.out.println("Enter ID number of employee to Update: ");
                                Scanner input = new Scanner(System.in);
                                int employeeUpdate = input.nextInt();

                                for (Employee employeeToUpdate : employees) {
                                    if (employeeUpdate == employeeToUpdate.getEmployeeNo()) {
                                        employeeToUpdate.update();
                                    }
                                }
                            }
                            break;

                            case 4: {
                                if (employees.isEmpty()) {
                                    System.out.println("NO RECORDS AVAILABLE");
                                }
                                else {
                                    for (Employee EmployeesToDisplay : employees) {
                                        System.out.printf("EMPLOYEE %d", (employees.indexOf(EmployeesToDisplay) + 1));
                                        EmployeesToDisplay.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 5: {
                                System.out.println("Enter ID of employee to Delete: ");
                                Scanner input = new Scanner(System.in);
                                int employeeDelete = input.nextInt();
                                boolean employeeFound = false;

                                for (Employee employeeToDelete : employees) {
                                    if (employeeDelete == employeeToDelete.getEmployeeNo()) {
                                        employees.remove(employeeToDelete);
                                        System.out.println("Record removed successfully");
                                        employeeFound = true;
                                        System.out.println();
                                        break;
                                    }

                                }
                                if (!employeeFound) {
                                    System.out.println("Record Not Found");
                                }
                            }
                            break;

                            case 6: {
                                System.out.println();
                                System.out.println("1.Show Allowance details");
                                System.out.println("2.Update Allowance details");
                                Scanner sc = new Scanner(System.in);
                                int allowanceOptions = sc.nextInt();

                                while (true) {
                                    switch (allowanceOptions) {
                                        case 1:
                                            employee.get_Allowance(1);
                                            employee.get_Allowance(2);
                                            break;
                                        case 2:
                                            employee.set_Allowance();
                                            break;
                                        default:
                                            System.out.println("Invalid option.");
                                            break;
                                    }
                                    if (allowanceOptions == 1 || allowanceOptions == 2) break;
                                }
                            }
                            break;

                            case 7:
                                break;

                            default:
                                System.out.println("INVALID CHOICE");

                        }

                    }
                    break;
                }
                case 3: {

                    while (choiceAtCentresMenu != 8) {

                        System.out.println();
                        System.out.println("\t\t\tWELCOME TO THE NTS TEST CENTRE PORTAL");
                        System.out.println();
                        System.out.println("1.ADD TEST CENTRE DATA");
                        System.out.println("2.SEARCH TEST CENTRE DATA");
                        System.out.println("3.UPDATE TEST CENTRE DATA");
                        System.out.println("4.DISPLAY TEST CENTRE DATA");
                        System.out.println("5.DELETE TEST CENTRE DATA");
                        System.out.println("6.ADD DUTY TEAM");
                        System.out.println("7.SHOW SUPERVISOR DETAILS");
                        System.out.println("8.EXIT");


                        while (true) {
                            Scanner input = new Scanner(System.in);
                            try {
                                System.out.println("Enter your choice: ");
                                input = new Scanner(System.in);
                                choiceAtCentresMenu = input.nextInt();
                                input.nextLine();
                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid Input.Try again.");
                                input.nextLine();
                            }
                        }

                        switch (choiceAtCentresMenu) {
                            case 1: {
                                TestCentre testCentre1 = new TestCentre();
                                testCentre1.setter();
                                testCentres.add(testCentre1);
                                System.out.println("ADDED SUCCESSFULLY");
                                System.out.println();
                            }
                            break;

                            case 2: {
                                System.out.println("Enter number of testCentre to search: ");
                                Scanner input = new Scanner(System.in);
                                int testCentreSearch = input.nextInt();

                                for (TestCentre testCentreToSearch : testCentres) {
                                    if (testCentreSearch == testCentreToSearch.getTestCentreNo()) {
                                        testCentreToSearch.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 3: {
                                System.out.println("Enter number of testCentre to Update: ");
                                Scanner input = new Scanner(System.in);
                                int testCentreUpdate = input.nextInt();

                                for (TestCentre testCentreToUpdate : testCentres) {
                                    if (testCentreUpdate == testCentreToUpdate.getTestCentreNo()) {
                                        testCentreToUpdate.update();
                                    }
                                }
                            }
                            break;

                            case 4: {
                                if (testCentres.isEmpty()) {
                                    System.out.println("NO RECORDS AVAILABLE");
                                }
                                else {
                                    for (TestCentre TestCentresToDisplay : testCentres) {
                                        System.out.printf("TEST CENTRE %d", (testCentres.indexOf(TestCentresToDisplay) + 1));
                                        TestCentresToDisplay.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 5: {
                                System.out.println("Enter ID of testCentre to Delete: ");
                                Scanner input = new Scanner(System.in);
                                int testCentreDelete = input.nextInt();
                                boolean testCentreFound = false;

                                for (TestCentre testCentreToDelete : testCentres) {
                                    if (testCentreDelete == testCentreToDelete.getTestCentreNo()) {
                                        testCentres.remove(testCentreToDelete);
                                        System.out.println("Record removed successfully");
                                        testCentreFound = true;
                                        System.out.println();
                                        break;
                                    }

                                }
                                if (!testCentreFound) {

                                    System.out.println("Record Not Found");
                                }
                            }
                            break;

                            case 6: {
                                Scanner input = new Scanner(System.in);
                                System.out.println("YOU CAN ADD\n1.INVIGILATORS\n2.SUPERINTENDENT");
                                System.out.println("Enter your choice: ");
                                int choiceAtDutyTeam = input.nextInt();

                                while (true) {
                                    switch (choiceAtDutyTeam) {
                                        case 1:
                                            testCentre.addInvigilators();
                                            break;
                                        case 2:
                                            testCentre.addSupervisor();
                                            break;
                                        default:
                                            System.out.println("Invalid Input");
                                            break;
                                    }
                                    if (choiceAtDutyTeam == 1 || choiceAtDutyTeam == 2) break;
                                }
                            }
                            break;


                            case 7: {
                                testCentre.showSupervisorDetails();
                            }
                            break;

                            case 8:
                                break;

                            default:
                                System.out.println("Invalid choice");
                        }

                    }
                    break;
                }

                case 4: {

                    while (choiceAtTestMenu != 7) {

                        System.out.println();
                        System.out.println("\t\t\tWELCOME TO THE NTS TEST PORTAL");
                        System.out.println();
                        System.out.println("1.ADD TEST DATA");
                        System.out.println("2.SEARCH TEST DATA");
                        System.out.println("3.UPDATE TEST DATA");
                        System.out.println("4.DISPLAY TEST DATA");
                        System.out.println("5.DELETE TEST DATA");
                        System.out.println("6.SHOW TEST DETAILS");
                        System.out.println("7.EXIT");


                        while (true) {
                            Scanner input = new Scanner(System.in);
                            try {
                                System.out.println("Enter your choice: ");
                                input = new Scanner(System.in);
                                choiceAtTestMenu = input.nextInt();
                                input.nextLine();
                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid Input.Try again.");
                                input.nextLine();
                            }
                        }

                        switch (choiceAtTestMenu) {
                            case 1: {
                                Test test1 = new Test();
                                test1.setter();
                                Candidate.tests.add(test1);
                                System.out.println("ADDED SUCCESSFULLY");
                                System.out.println();
                            }
                            break;

                            case 2: {
                                System.out.println("Enter ID of test to search: ");
                                Scanner input = new Scanner(System.in);
                                int testSearch = input.nextInt();

                                for (Test testToSearch : Candidate.tests) {
                                    if (testSearch == testToSearch.getTestId()) {
                                        testToSearch.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 3: {
                                System.out.println("Enter ID of test to Update: ");
                                Scanner input = new Scanner(System.in);
                                int testUpdate = input.nextInt();

                                for (Test testToUpdate : Candidate.tests) {
                                    if (testUpdate == testToUpdate.getTestId()) {
                                        testToUpdate.update();
                                    }
                                }
                            }
                            break;

                            case 4: {
                                if (Candidate.tests.isEmpty()) {
                                    System.out.println("NO RECORDS AVAILABLE");
                                }
                                else {
                                    for (Test TestsToDisplay : Candidate.tests) {
                                        System.out.printf("TEST %d", (Candidate.tests.indexOf(TestsToDisplay) + 1));
                                        TestsToDisplay.display();
                                        System.out.println();
                                    }
                                }
                            }
                            break;

                            case 5: {
                                System.out.println("Enter ID of test to Delete: ");
                                Scanner input = new Scanner(System.in);
                                int testDelete = input.nextInt();
                                boolean testFound = false;

                                for (Test testToDelete : Candidate.tests) {
                                    if (testDelete == testToDelete.getTestId()) {
                                        Candidate.tests.remove(testToDelete);
                                        System.out.println("Record removed successfully");
                                        testFound = true;
                                        System.out.println();
                                        break;
                                    }

                                }
                                if (!testFound) {
                                    System.out.println("Record Not Found");
                                }
                            }
                            break;

                            case 6: {
                                test.checkAvailableTests();
                            }
                            break;

                            case 7:
                                break;

                            default:
                                System.out.println("INVALID CHOICE");

                        }
                    }
                    break;
                }

                case 0:
                    System.out.println("EXITING THE PORTAL");
                    break;


                default:
                    System.out.println("Invalid choice");
            }

        }

    }

}
