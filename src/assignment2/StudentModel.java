package assignment2;

public class StudentModel {
    private int SID;
    private int STUD_ID;
    private String fName;
    private String lName;

    public StudentModel(int STU_ID) {
        this.STUD_ID = STU_ID;
    }

    public int getSID() {
        return SID;
    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public int getSTU_ID() {
        return STUD_ID;
    }

    public void setSTU_ID(int STU_ID) {
        this.STUD_ID = STU_ID;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
