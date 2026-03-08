package smartplanner.model;

import java.util.Objects;

public class Profile {
    private String studentName;
    private String major;
    private int preferredDailyStudyMinutes;

    public Profile() {
        this("", "", 120);
    }

    public Profile(String studentName, String major, int preferredDailyStudyMinutes) {
        setStudentName(studentName);
        setMajor(major);
        setPreferredDailyStudyMinutes(preferredDailyStudyMinutes);
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = Objects.requireNonNullElse(studentName, "").trim();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = Objects.requireNonNullElse(major, "").trim();
    }

    public int getPreferredDailyStudyMinutes() {
        return preferredDailyStudyMinutes;
    }

    public void setPreferredDailyStudyMinutes(int preferredDailyStudyMinutes) {
        if (preferredDailyStudyMinutes <= 0) {
            throw new IllegalArgumentException("Preferred daily study minutes must be greater than 0.");
        }
        this.preferredDailyStudyMinutes = preferredDailyStudyMinutes;
    }
}
