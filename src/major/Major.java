package major;

public class Major {

    private String major_id;
    private String major_name;
    private String classification;



    public Major(final String major_id, final String major_name, final String classification ) {
        this.major_id = major_id;
        this.major_name = major_name;
        this.classification = classification;

    }

    public String getMajor_id() {
        return major_id;
    }

    public String getMajor_name() {
        return major_name;
    }

    public String getClassification() {
        return classification;
    }

    @Override
    public String toString() {
        return "Major{" +
                "major_id='" + major_id + '\'' +
                ", major_name='" + major_name + '\'' +
                ", classification='" + classification + '\'' +
                '}';

    }
}