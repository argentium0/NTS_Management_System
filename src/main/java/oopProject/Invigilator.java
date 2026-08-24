package oopProject;

public class Invigilator extends Employee {

    private String designation;
    private String superintendentName;
    private Long supdtPhone;

    public Invigilator() {
        super();
        this.designation = "";
        this.superintendentName = "";
        this.supdtPhone = 0L;
    }

    public Invigilator(String n, String fn, String id, String phone, int employeeID, String employeeCity, float allowance, int experience, Double invig_allowance, Double spdt_allowance, String designation, String superintendentName, Long supdtPhone) {
        super(n, fn, id, phone, employeeID, employeeCity, allowance, experience, invig_allowance, spdt_allowance);
        this.designation = designation;
        this.superintendentName = superintendentName;
        this.supdtPhone = supdtPhone;
    }

    public Invigilator(String n, String fn, String id, String phone, int employeeID, String employeeCity, String des, String supervisor, Double invig_allowance, Double spdt_allowance, int interval) {
        super(n, fn, id, phone, employeeID, employeeCity, 0.0f, 0, invig_allowance, spdt_allowance);
        this.designation = des;
        this.superintendentName = supervisor;
        this.supdtPhone = 0L;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSuperintendentName() {
        return superintendentName;
    }

    public void setSuperintendentName(String superintendentName) {
        this.superintendentName = superintendentName;
    }

    public Long getSupdtPhone() {
        return supdtPhone;
    }

    public void setSupdtPhone(Long supdtPhone) {
        this.supdtPhone = supdtPhone;
    }

    public void delete() {
        // Method stub for UML delete logic
    }

    @Override
    public void setter() {}

    @Override
    public void update() {}

    @Override
    public void display() {
        super.display();
        System.out.println("Designation: " + designation + ", Superintendent: " + superintendentName);
    }
}
