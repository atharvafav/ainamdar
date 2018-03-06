import java.util.Arrays;

public class DataChecksum {
	public DataChecksum() {
		
	}
       public static boolean dataintegritycheck(byte []data)
       {
    	int i,n;                                  //initialization
    	byte last=(byte) (data[3]^data[4]);
   		for(int j=5;j<=301;j++)                   // Ex-oring the data bytes first
   		{
   			last=(byte) (last^data[j]);
   		}
   		int []binarypayload=new int[16];          
   		i=15;
   		if(last<0)
   			{n=last+256;}                         //Adding 256 if the value is negative number
   		else n=last;
   		while(n!=0)
   		{
   			binarypayload[i]=n%2;                 //Converting payload into binary data 
   		   		i--;
   		   		n=n/2;
   		}
//////////////////////////////////////////////////////////////
   		n=data[0];	
		i=15;                                    //Counter for 16 bits
		int []packet_type=new int[16]; 
		while(n!=0)
		{
		   		packet_type[i]=n%2;              //Converting packet type from 51(33h) to its binary form
		   		i--;
		   		n=n/2;
		} 
/////////////////////////////////////////////////////////////
		int []checksum=new int[16];   
		i=7;                                     //Counter for 8 bits
	        
		   if(data[303]<0)  n=data[303]+256;
		   else n=data[303];
		    while(n!=0)
			{
			   		checksum[i]=n%2;             //Converting most significant byte of integrity check to its binary form
			   		i--;
			   		n=n/2;
			}
		    i=15;                                //Counter for 16 bits
		    if(data[304]<0)  n=data[304]+256;
			   else n=data[304];
		    while(n!=0)
			{
			   		checksum[i]=n%2;             //Converting least significant byte of integrity check to its binary form
			   		i--;
			   		n=n/2;
			} 
///////////////////////////////////////////////////////////////////		
		    int []sequence_number=new int[16];   
			i=7;                                 //Counter for 8 bits
		        
			    
			   if(data[1]<0) n=data[1]+256;
			   else n=data[1];
			    while(n!=0)
				{
				   		sequence_number[i]=n%2;  //Converting most significant byte of sequence number field to its binary form
				   		i--;
				   		n=n/2;
				}
			    i=15;                            //Counter for 16 bits
			    if(data[2]<0) n=data[2]+256;
				   else n=data[2];

			    while(n!=0)
				{
				   		sequence_number[i]=n%2;  //Converting least significant byte of integrity check to its binary form
				   		i--;
				   		n=n/2;
				}
/////////////////////////////////////////////////////////////////////////   
			    int []check=new int[16];
			    for(i=0;i<16;i++)
		        {
		        	check[i]=packet_type[i]^sequence_number[i]^binarypayload[i]^checksum[i];  //checksum of all the fields in data packet
		                
		        }
			   int power=0,checksum_check=0;
			   for(i=15;i>=0;i--)
			   {
				   checksum_check=(int) (checksum_check+check[i]*Math.pow(2,power));
				   power++;
			   }

			   if(checksum_check==0)                             // check if checksum is 0 any non zero value means there is error in packet
			   return true;    
			   else 
			  return false;
       }
}
