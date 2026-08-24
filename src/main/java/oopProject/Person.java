package oopProject;

public abstract class Person {

    protected String name;
    protected String fname;
    protected String idCard;
    protected String phoneNo;

    Person()
    {
        name = null;
        fname = null;
        idCard=null;
        phoneNo=null;
    }

    Person(String n,String fn,String id,String phone)
    {
        this.name = n;
        this.fname=fn;
        this.idCard=id;
        this.phoneNo=phone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public abstract void setter();

    public abstract void display();

    public abstract void update();

}

