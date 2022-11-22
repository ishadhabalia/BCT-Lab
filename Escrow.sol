pragma solidity >=0.7.0 <0.9.0;
contract Escrow{
    address public payer;
    address payable public payee;
    address public thirdParty;
    uint public amount;
 
    constructor(address sender,address rec,uint amt){
        payer = sender;
        payee = payable(rec);
        amount = amt * 10**18;
        thirdParty = msg.sender;
    }
 
    function deposit() public payable{
        require(msg.sender == payer,"Sender must be the payer");
        require(address(this).balance<=amount,"Cant send more than escrow amount");
    }
 
    function release() public {
        require(amount == address(this).balance,"cannot release funds before full amount is sent");
        require(msg.sender == thirdParty,"only thirdParty can release funds");
        payee.transfer(amount);
    }
 
    function balanceOf() public view returns(uint){
        return address(this).balance;
    }
}
 
