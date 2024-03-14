public class Member {
    private String firstName;
    private String lastName;
    private long personalNumber;
    private int memberID;
    private boolean suspension;
    private int maxNumOfBooks;
    private int currentNumOfBooks = 0;


    public Member(String firstName, String lastName, long personalNumber, int memberID, int maxNumOfBooks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
        this.memberID = memberID;
        this.maxNumOfBooks = maxNumOfBooks;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public boolean isSuspension() {
        return suspension;
    }

    public void setSuspension(boolean suspension) {
        this.suspension = suspension;
    }

    public int getMaxNumOfBooks() {
        return maxNumOfBooks;
    }

    public void setMaxNumOfBooks(int maxNumOfBooks) {
        this.maxNumOfBooks = maxNumOfBooks;
    }

    public int getCurrentNumOfBooks() {
        return currentNumOfBooks;
    }

    public void setCurrentNumOfBooks(int currentNumOfBooks) {
        this.currentNumOfBooks = currentNumOfBooks;
    }

}
