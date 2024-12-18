package reputation;

public class Reputation {

    private String student_id;
    private Integer valuation_score;
    private String review;
    private Integer reputation_id;

    public Reputation(final String student_id, final Integer valuation_score,
                      final String review, final Integer reputation_id){
        this.student_id = student_id;
        this.valuation_score = valuation_score;
        this.review = review;
        this.reputation_id = reputation_id;

    }

    public String getStudent_id() { return student_id; }

    public Integer getValuation_score() { return valuation_score; }

    public String getReview() { return review; }

    public Integer getReputation_id() { return reputation_id; }

    @Override
    public String toString(){
        return "Reputation ["+
                "student_id='" + student_id + '\'' +
                ", valuation_score='" + valuation_score + '\'' +
                ", review= '" + review + '\'' +
                ", reputation_id='" + reputation_id + '\''+
                '}';
    }
}
