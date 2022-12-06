import Web3 from 'web3';
import configuration from '../build/contracts/EcommerceContract.json';
import 'bootstrap/dist/css/bootstrap.css';
import jeans from './images/jeans.jpg';
import dress from './images/dress.jpg';
import jacket from './images/jacket.jpg';
import joggers from './images/joggers.jpg';


const image_names_strings = ["jeans","dress","jacket","joggers"];      
const image_names = [jeans,dress,jacket,joggers];     

const createElementFromString = (string) => {
  const el = document.createElement('div');
  el.innerHTML = string;
  return el.firstChild;
};

const CONTRACT_ADDRESS =
  configuration.networks['5777'].address;
const CONTRACT_ABI = configuration.abi;


const web3 = new Web3(
  Web3.givenProvider || 'http://127.0.0.1:7545'
);
const contract = new web3.eth.Contract(
  CONTRACT_ABI,
  CONTRACT_ADDRESS
);

let account;

const accountEl = document.getElementById('account');
const garmentsEl = document.getElementById('garments');
const TOTAL_garmentS = 8;
const EMPTY_ADDRESS =
  '0x0000000000000000000000000000000000000000';


  const buy = async (garment) => {
    await contract.methods.buy(garment.id).send({ from: account,value: garment.garmentPrice });
    
  };
  
  // const transac = async (garment) => {
  //  console.log("abc");
    
  // };

var imagename;
const refreshgarments = async () => {
  garmentsEl.innerHTML = '';
  for (let i = 0; i < TOTAL_garmentS; i++) {
    const garment = await contract.methods.garments(i).call();
    console.log(garment);
    for(let j=0;j<image_names.length;j++){
      if(image_names_strings[j]==garment.garmentName){
        imagename = image_names[j];
        break;
      }
    }
    console.log(imagename)
    garment.id = i;
    if (garment.buyer === EMPTY_ADDRESS) {
      const garmentEl = createElementFromString(
        
        `<div class="garment card h-100 shadow-sm">
        <a href="#">
          <img src="${imagename}" class="card-img-top" alt="product.title" />
        </a>

        <div class="label-top shadow-sm" style="color:black">
        ${garment.garmentName}
        </div>
        <div class="card-body">
          <div class="clearfix mb-3">
            <span class="float-start badge rounded-pill bg-success"> ${
              garment.garmentPrice / 1e18
            } Eth</span>

            <span class="float-end">Label Size: ${garment.labelSize}</span>
          </div>
          <h5 class="card-title">
            About the product: ${garment.garmentDesc}
          </h4>
          <h5 class="card-title">
            Condition: ${garment.garmentCond}
          </h5>

          <div class="d-grid gap-2 my-4">

            <a href="#" class="btn btn-warning bold-btn">Buy</a>

          </div>
          <div class="clearfix mb-1">

            <span class="float-start"><a href="#"><i class="fas fa-question-circle"></i></a></span>

            <span class="float-end">
              <i class="far fa-heart" style="cursor: pointer"></i>

            </span>
          </div>
        </div>
      </div>`
        
      );
      garmentEl.onclick = buy.bind(null, garment);
      garmentsEl.appendChild(garmentEl);
          }
  }
};

const main = async () => {
  const accounts = await web3.eth.requestAccounts();
  account = accounts[0];
  accountEl.innerText = account;
  await refreshgarments();
};

main();