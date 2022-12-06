pragma solidity >=0.7.0 <0.9.0;
 
contract EcommerceContract{
    struct garment{
        string garmentName;
        string garmentDesc;
        string garmentCond;
        string labelSize;
        uint garmentId;
        uint garmentPrice;
        string garmentImage;
        address payable seller;
        address buyer;
    }
 
    garment[] public garments;
    uint id=0;
 
    function registergarment(string memory name, string memory desc, string memory cond, string memory size, uint price,string memory img) public{
        require(price>0,"Price of a garment cannot be negative!");
        garment memory prod;
        prod.garmentName=name;
        prod.garmentDesc=desc;
        prod.garmentId=id;
        id=id+1;
        prod.garmentPrice=price*10**18;
        prod.garmentCond=cond;
        prod.labelSize=size;
        prod.garmentImage=img;
        prod.seller=payable(msg.sender);
        garments.push(prod);
    }
 

     function buy(uint256 _index) external payable {
     require(msg.value >= garments[_index].garmentPrice);
    garments[_index].buyer = msg.sender; 
  } 
}
 

