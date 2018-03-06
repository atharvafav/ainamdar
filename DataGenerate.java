import java.util.Arrays;
import java.util.Random;
public class DataGenerate {
	public DataGenerate() {
		
	}
	public static byte[] datagenerate(int seq_no)
	{
		int n,i;   //Initialization
		Random randomdata=new Random(); //Random data generation
		byte []datapac=new byte[305];   //Byte array for data packet 
		datapac[0]=51;                  //Assigning 51 as packet type value 
		n=datapac[0];	                //Storing packet type value in a variable for further processing
		i=15;
		int []packet_type=new int[16]; 
		while(n!=0)
		{
		   		packet_type[i]=n%2;     //Converting packet type value to binary form
		   		i--;
		   		n=n/2;
		} 
////////SEQUENCE NUMBER GENERATOR/////////////////////////////////
	
		n=seq_no;                      //Storing sequence number in a variable for processing
		i=15;                          //Counter for 16 bits
		int []sequence_number=new int[16]; 
		while(n!=0)
		{
		   		sequence_number[i]=n%2;//Converting sequence number to binary form
		   		i--;
		   		n=n/2;
		} 
		int power=0;
		for(i=7;i>=0;i--)
		{
			datapac[1]=(byte) (datapac[1]+sequence_number[i]*Math.pow(2,power));//Sequence No to bytes
			power++;
		}
		power=0; 
		for(i=15;i>=8;i--)
		{
			datapac[2]=(byte) (datapac[2]+sequence_number[i]*Math.pow(2,power));//Sequence No to bytes
			power++;
		}
		
////////////random data generator 300 bytes data///////////////////////////
	
		byte[] data=new byte [300];          //Byte array to store 300 bytes of randomly generated data
		randomdata.nextBytes(data);
		int k=2;
		for(int j=0; j<=299;j++)
		{
			k++;
			datapac[k]=data[j];              //Copying the data from data array to data packet array
		}
		byte last=(byte) (data[0]^data[1]);
		for(int j=2;j<=298;j++)
		{
			last=(byte) (last^data[j]);     //Ex-oring the 300 bytes of data before ex-oring with other fields
		}
		int []binarypayload=new int[16]; 
		i=15;
		if(last<0) {n=last+256;}
		else n=last;
		while(n!=0)
		{
			binarypayload[i]=n%2;            //Converting the payload data bytes to binary form
		   		i--;
		   		n=n/2;
		}
		
////////  CHECKSUM CREATE  ///////////////////////////////////////////////////
		int []checksum=new int[16];
        for(i=0;i<16;i++)
        {
        	checksum[i]=packet_type[i]^sequence_number[i]^binarypayload[i];//ex-oring all the fields of data packet
        }
        power=0;
		for(i=7;i>=0;i--)
		{
			datapac[303]=(byte) (datapac[303]+checksum[i]*Math.pow(2,power));//Integrity check to bytes
			power++;
		}
		power=0; 
		for(i=15;i>=8;i--)
		{
			datapac[304]=(byte) (datapac[304]+checksum[i]*Math.pow(2,power)); //Integrity check to bytes
			power++;
		}
		
		return datapac;
	}
}
