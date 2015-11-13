package helperclasses;

/**
 * Created by prashantkumar on 09/11/15.
 */
public class Products
{
    String productName;
    String productSku;
    String productId;
    String productRegularPrice;
    String productSalePrice;
    String imageUrl;
    String description;
    String condition;

    public Products(String productName, String productSku,String productId,String productRegularPrice,String productSalePrice,String imageUrl,String description) {
        this.productName = productName;
        this.productSku = productSku;
        this.productId=productId;
        this.productRegularPrice = productRegularPrice;
        this.productSalePrice=productSalePrice;
        this.imageUrl=imageUrl;
        this.description=description;
    }

    public Products(String condition, String productSalePrice, String productRegularPrice) {
        this.condition = condition;
        this.productSalePrice = productSalePrice;
        this.productRegularPrice = productRegularPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public String getProductRegularPrice() {
        return productRegularPrice;
    }

    public String getProductSalePrice() {
        return productSalePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }
}
