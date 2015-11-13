package helperclasses;

/**
 * Created by prashantkumar on 12/11/15.
 */
public class BestProductDetail
{
    String description;
    String imageUrl;
    String productName;
    String price;
    String sku;

    public BestProductDetail(String description, String imageUrl, String productName, String price,String sku) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.price = price;
        this.sku=sku;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }
}
