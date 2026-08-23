package oopProject;

public class NamePrinter<Admin>{

    private Admin name;

    public Admin getName() {
        return name;
    }

    public void setName(Admin name) {
        this.name = name;
    }

   public void display()
   {
       System.out.println("Name: "+name);
   }
}
