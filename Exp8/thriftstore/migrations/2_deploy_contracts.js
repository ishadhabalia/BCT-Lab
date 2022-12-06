var EcommerceContract = artifacts.require('./EcommerceContract.sol');

module.exports = function(deployer) {
  deployer.deploy(EcommerceContract);
};
