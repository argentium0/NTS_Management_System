package oopProject;

public class Superintendent extends Employee {

    private int interval;

    public Superintendent() {
        super();
        this.interval = 0;
    }

    public Superintendent(String n, String fn, String id, String phone, int employeeID, String employeeCity, float allowance, int experience, Double invig_allowance, Double spdt_allowance, int interval) {
        super(n, fn, id, phone, employeeID, employeeCity, allowance, experience, invig_allowance, spdt_allowance);
        this.interval = interval;
    }

    public Superintendent(String n, String fn, String id, String phone, int employeeID, String city, Double invig_allowance, Double spdt_allowance, int interval) {
        super(n, fn, id, phone, employeeID, city, 0.0f, 0, invig_allowance, spdt_allowance);
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
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
        System.out.println("Interval: " + interval + " months");
    }
}
