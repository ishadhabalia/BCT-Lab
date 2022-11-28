pragma solidity >=0.7.0 <0.9.0;

contract EcommerceContract{
    struct Product{
        string productName;
        string productDesc;
        uint productId;
        uint productPrice;
        address payable seller;
        address buyer;
        bool delivery_ack;
    }

    Product[] public products;
    uint id=1;

    function registerProduct(string memory name, string memory desc, uint price) public{
        require(price>0,"Price of a product cannot be negative!");
        Product memory prod;
        prod.productName=name;
        prod.productDesc=desc;
        prod.productId=id;
        id=id+1;
        prod.productPrice=price * 10**18;
        prod.seller=payable(msg.sender);
        products.push(prod);
    }

    function buy(uint prodId) public payable{
        require(products[prodId-1].productPrice==msg.value,"The amount you are trying to pay is not equal to the price of the product!");
        products[prodId-1].buyer=msg.sender;
    }

    function delivery(uint prodId) public {
        require(products[prodId-1].buyer==msg.sender,"Only buyer can acknowledge delivery of the product!");
        products[prodId-1].delivery_ack=true;
        products[prodId-1].seller.transfer(products[prodId-1].productPrice);
    }
}
