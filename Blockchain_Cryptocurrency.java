import java.util.*;
import java.io.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;

class Transaction{
 String sender;
 String rec;
 double amount;
 int unspent;
 Transaction(String s,String r,double a){
    sender=s;
    rec=r;
    amount=a;
    unspent=0;
 }
}

class Block{
Transaction data;
String currhash;
String prevhash;
int nonce;
double reward;
Block(String s,String r,int a){
    data=new Transaction(s,r,a);
    prevhash=null;
    currhash=null;
    nonce=-1;
}
}

class Sign{
    String user;
    PrivateKey privKey;
    PublicKey pubKey;
    Sign(String s){
        user=s;
    }
}

public class Blockchain_Cryptocurrency{
ArrayList<Block> blockchain=new ArrayList<Block>();
ArrayList<Transaction> transaction_list=new ArrayList<Transaction>();
ArrayList<Sign> user_signs=new ArrayList<Sign>();
public static void main(String args[]){

Blockchain_Cryptocurrency obj = new Blockchain_Cryptocurrency();

obj.generateSignPair("isha");
obj.generateSignPair("john");
obj.generateSignPair("sarah");
obj.generateSignPair("sam");
obj.generateSignPair("me");
obj.generateSignPair("miner");
obj.generateSignPair("blk");

System.out.println("------------------Users----------------\n");
obj.displaySigns();
System.out.println("---------------------------------------\n");

System.out.println("------------------Initial Balance/Transactions----------------");
obj.transaction_list.add(new Transaction("isha","john",100));
obj.transaction_list.add(new Transaction("isha","sarah",100));
obj.transaction_list.add(new Transaction("isha","sam",100));
obj.transaction_list.add(new Transaction("isha","blk",100));
obj.displayTransaction();
System.out.println("---------------------------------------\n");

obj.addToBlockChain("john","me",40);
obj.addToBlockChain("sarah","me",70);
obj.addToBlockChain("sam","me",10);
obj.addToBlockChain("me","shop",50);
obj.addToBlockChain("me","shop",120);

System.out.println("------------------Blockchain---------------------\n");
obj.displayBlockChain();
System.out.println("--------------------UTXO-------------------\n");
obj.displayTransaction();

}

public void generateSignPair(String user) {
try{
Sign new_pair=new Sign(user);
KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");

keyPairGen.initialize(2048);
KeyPair pair = keyPairGen.generateKeyPair();
PrivateKey Rk = pair.getPrivate();   
new_pair.privKey= Rk;
PublicKey Pu = pair.getPublic();   
new_pair.pubKey= Pu;
user_signs.add(new_pair);
}
catch (NoSuchAlgorithmException e) {
    System.out.println("Exception thrown for incorrect algorithm: " + e);
}
}

public byte[] signData(String user,String data) 
{
    byte[] signature=null;
    try{


int index=-1;
for(int i=0;i<user_signs.size();i++){
    if(user_signs.get(i).user.equals(user)){
        index=i;
        break;
    }
}
PrivateKey privKey=user_signs.get(index).privKey;

Signature sign = Signature.getInstance("SHA256withDSA");
sign.initSign(privKey);
byte[] bytes = data.getBytes();    
sign.update(bytes);
signature = sign.sign();
    }
    catch (NoSuchAlgorithmException e) {
        System.out.println("Exception thrown for incorrect algorithm: " + e);
    }
    catch (InvalidKeyException e) {
        System.out.println("Exception thrown for incorrect algorithm: " + e);
    }
    catch (SignatureException e) {
        System.out.println("Exception thrown for incorrect algorithm: " + e);
    }
return signature;

}

public static byte[] getSHA(String input)
    {   try{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    catch (NoSuchAlgorithmException e) {
        System.out.println("Exception thrown for incorrect algorithm: " + e);
    }
    return null;
    }
     
    public static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }

public ArrayList<String> pow(String s,String r,int a){
int nonce=-1;
String hash="";
ArrayList<String> nonce_hash=new ArrayList<String>();

int flag=0;
while(flag==0){
    
    String data=s+r+String.valueOf(a)+String.valueOf(nonce);
    
    hash=toHexString(getSHA(data));
    
    
    if(hash.startsWith("000")){
        flag=1;
        nonce_hash.add(String.valueOf(nonce));
        nonce_hash.add(hash);
    }
    nonce=nonce+1;
}

return nonce_hash;
}

public int verifysignature(String user,String data,byte[] signature) 
{
int flag=0;
try{
Signature sign = Signature.getInstance("SHA256withDSA");
flag=0;
int index=-1;
for(int i=0;i<user_signs.size();i++){
    if(user_signs.get(i).user.equals(user)){
        index=i;
        break;
    }
}
PublicKey pubKey=user_signs.get(index).pubKey;
sign.initVerify(pubKey);
byte[] bytes = data.getBytes();    
sign.update(bytes);
boolean bool = sign.verify(signature);
if(bool==true) flag=1;
return flag;
}
catch (NoSuchAlgorithmException e) {
    System.out.println("Exception thrown for incorrect algorithm: " + e);
}
catch (InvalidKeyException e) {
    System.out.println("Exception thrown for incorrect algorithm: " + e);
}
catch (SignatureException e) {
    System.out.println("Exception thrown for incorrect algorithm: " + e);
}
return flag;
}

public int utxoModel(String s,int a){
    double total=0;
    int flag=0;
    ArrayList<Transaction> valid=new ArrayList<Transaction>();
    for(int i=0;i<transaction_list.size();i++){
        Transaction current=transaction_list.get(i);
        if(current.rec.equals(s) && current.unspent==0){
            total=total+current.amount;
            valid.add(current);
            transaction_list.remove(current);
            if(total>=a) break;
        }
    }
    
    double value=0;
    for(int i=0;i<valid.size();i++) value=value+valid.get(i).amount;
    
    if(value>=a){
        flag=1;
        for(int i=0;i<valid.size();i++) valid.get(i).unspent=1;
        if(value>a){
        Transaction rem = new Transaction(s,s,value-a);
        transaction_list.add(rem);
        }
    }
  
    transaction_list.addAll(valid);

    return flag;
}

public int verifyPOW(String hash){
int flag=0;
if(hash.startsWith("000")) flag=1;
return flag; 
}

public void addToBlockChain(String s,String r,int a) 
{
    System.out.println("------------------Transaction----------------");
    System.out.println(s+" to "+ r +" -> "+a+"\n");
    Block b = new Block(s,r,a); 
    ArrayList<String> nonce_hash=new ArrayList<String>();
    nonce_hash=pow(s,r,a);
    int n=Integer.valueOf(nonce_hash.get(0));
    b.nonce=n;
    String hash=nonce_hash.get(1);
    b.currhash=hash;
    System.out.println("------------------Hashing----------------\n");
    System.out.println("Hash: "+hash+"\n");
    if(blockchain.size()>0){
    Block prev = blockchain.get(blockchain.size() - 1);
    b.prevhash=prev.currhash;
    }
    int signature_flag=0;
    byte[] signature=null;
    
    System.out.println("-----------------Signing using private key----------------\n");
    signature = signData(s,hash);
    System.out.println("Signed Data" +toHexString(signature)+"\n");
    System.out.println("-----------------Verifying Signature----------------\n");
    signature_flag=verifysignature(s,hash,signature);
    System.out.println("-----------------Signature Verified using public key----------------\n");
    

    int utxo_flag=utxoModel(s,a);
    int transact_flag=0;
    int pow_flag=verifyPOW(hash);
    // int pow_flag=1;
    
    if(utxo_flag==0) System.out.println("No sufficient UTXO present");
    if(signature_flag==0) System.out.println("Signature not valid");
    if(signature_flag==1 && utxo_flag==1) transact_flag=1;
    if(transact_flag==1 && pow_flag==1){
        blockchain.add(b);
        transaction_list.add(b.data);
        System.out.println("-----------------Block added to Blockchain----------------\n");
    System.out.println("Transaction: "+b.data.sender+"->"+b.data.rec+"="+b.data.amount); 
    System.out.println("Nonce: "+b.nonce); 
    System.out.println("Previous Hash: "+b.prevhash); 
    System.out.println("Current Hash: "+b.currhash);     
    System.out.println("--------------------------------");
    System.out.println("\n----------------POW verified. Reward Given to Miner----------------\n");
        double reward= 0.01 * a;
        Transaction t= new Transaction("blk","miner",reward);
        transaction_list.add(t);
        System.out.println("Transaction: "+"blk"+"-> miner "+"="+reward+"\n"); 
    }
    
    
    
}

public void displayBlockChain(){
    for(int i=0;i<blockchain.size();i++){
        Block current=blockchain.get(i);
        System.out.println("Transaction: "+current.data.sender+"->"+current.data.rec+"="+current.data.amount); 
        System.out.println("Nonce: "+current.nonce); 
        System.out.println("Previous Hash: "+current.prevhash); 
        System.out.println("Current Hash: "+current.currhash);     
        System.out.println("--------------------------------");
    }
}

public void displayTransaction(){
    for(int i=0;i<transaction_list.size();i++){
        Transaction current=transaction_list.get(i);
        System.out.println(current.sender+"->"+current.rec+"="+current.amount);  
        if(current.unspent==1) System.out.println("spent");
        else System.out.println("unspent");
        System.out.println("-----------------------------");
    }
}

public void displaySigns(){
    for(int i=0;i<user_signs.size();i++){
        Sign current=user_signs.get(i);
        System.out.println(current.user);  
        // System.out.println("Public Key"+current.pubKey);  
        System.out.println("-----------------------------");
    }
}

}






