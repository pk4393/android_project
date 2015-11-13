package helperclasses;

/**
 * Created by prashantkumar on 12/11/15.
 */
public class ProductReviewDetail
{
    String reviewerName;
    String productRating;
    String reviewComment;

    public ProductReviewDetail(String reviewerName, String productRating, String reviewComment) {
        this.reviewerName = reviewerName;
        this.productRating = productRating;
        this.reviewComment = reviewComment;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getProductRating() {
        return productRating;
    }

    public String getReviewComment() {
        return reviewComment;
    }
}
