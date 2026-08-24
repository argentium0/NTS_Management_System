package oopProject;

public class Employee extends Person implements AllowanceManagement, Allowance_Management {

    protected int employeeID;
    protected String employeeCity;
    protected float allowance;
    protected int experience;
    protected Double invig_allowance = 0.0;
    protected Double spdt_allowance = 0.0;

    public Employee() {
        super();
        employeeID = 0;
        employeeCity = "";
        allowance = 0.0f;
        experience = 0;
        invig_allowance = 0.0;
        spdt_allowance = 0.0;
    }

    public Employee(String n, String fn, String id, String phone, int employeeID, String employeeCity, float allowance, int experience, Double invig_allowance, Double spdt_allowance) {
        super(n, fn, id, phone);
        this.employeeID = employeeID;
        this.employeeCity = employeeCity;
        this.allowance = allowance;
        this.experience = experience;
        this.invig_allowance = invig_allowance;
        this.spdt_allowance = spdt_allowance;
    }

    // Secondary constructor for existing usages
    public Employee(String n, String fn, String id, String phone, int employeeID, String employeeCity, Double invig_allowance, Double spdt_allowance) {
        this(n, fn, id, phone, employeeID, employeeCity, 0.0f, 0, invig_allowance, spdt_allowance);
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    // Alias for backward compatibility
    public int getEmployeeNo() {
        return employeeID;
    }

    public void setEmployeeNo(int employeeNo) {
        this.employeeID = employeeNo;
    }

    public String getEmployeeCity() {
        return employeeCity;
    }

    public void setEmployeeCity(String employeeCity) {
        this.employeeCity = employeeCity;
    }

    // Alias for backward compatibility
    public String getCity() {
        return employeeCity;
    }

    public void setCity(String city) {
        this.employeeCity = city;
    }

    public float getAllowanceValue() {
        return allowance;
    }

    public void setAllowanceValue(float allowance) {
        this.allowance = allowance;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Double getInvig_allowance() {
        return invig_allowance;
    }

    public void setInvig_allowance(Double invig_allowance) {
        this.invig_allowance = invig_allowance;
    }

    public Double getSpdt_allowance() {
        return spdt_allowance;
    }

    public void setSpdt_allowance(Double spdt_allowance) {
        this.spdt_allowance = spdt_allowance;
    }

    @Override
    public void getAllowance() {
        System.out.println("Invigilator Allowance: " + invig_allowance + ", Superintendent Allowance: " + spdt_allowance);
    }

    @Override
    public void setAllowance() {
        // Base setAllowance stub
    }

    @Override
    public void get_Allowance(int choice) {
        if (choice == 1) {
            System.out.println("Invigilator Allowance: " + invig_allowance);
        } else {
            System.out.println("Superintendent Allowance: " + spdt_allowance);
        }
    }

    @Override
    public void set_Allowance() {
        setAllowance();
    }

    @Override
    public void setter() {}

    @Override
    public void display() {
        System.out.println("Employee ID: " + employeeID + ", City: " + employeeCity);
    }

    @Override
    public void update() {}
}
