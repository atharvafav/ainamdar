import java.util.Arrays;

public class InitChecksum {
	public InitChecksum() {
	}
	public static boolean initintegritycheck(byte[] input)
	{
		int fieldvalues,i;                         
		fieldvalues=input[0];	
		i=15;
		int []packet_type=new int[16]; 
		while(fieldvalues!=0)
		{
		   		packet_type[i]=fieldvalues%2;  //Converting packet type from 85(55h) to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		} 	
	    int []sequence_number=new int[16];     //Initialization 
	    int []no_of_packets=new int[16];       //Initialization 
	    int []payload=new int[16];             //Initialization 
	    int []checksum=new int[16];            //Initialization 
	    int []input_int=new int[9];            //Initialization 
////////Converting bytes(which may be -ve) to 8-bit positive decimals//////
	    for(i=0;i<9;i++)
	    {
	    	if(input[i]<0)
	    	input_int[i]=input[i]+256;
	    	else                               
	    	input_int[i]=input[i];
	    }
///////////////////////////////////////////////////////////////////////////
	    i=7;
	    fieldvalues=input_int[1];
	    while(fieldvalues!=0)
		{
		   		sequence_number[i]=fieldvalues%2;    //Converting value of least significant byte[2nd byte] to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		}
	    i=15;
	    fieldvalues=input_int[2];
	    while(fieldvalues!=0)
		{
		   		sequence_number[i]=fieldvalues%2;    //Converting value of most significant byte[1st byte] to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		}
///////////////////////////////////////////////////////////////////////	   
	    i=15;
	    fieldvalues=input_int[4];
	    while(fieldvalues!=0)
		{
		   		no_of_packets[i]=fieldvalues%2;      //Converting value of most significant byte[4th byte] to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		}

	    i=7;
	    fieldvalues=input_int[5];
	    while(fieldvalues!=0)
		{
		   		payload[i]=fieldvalues%2;            //Converting value of least significant byte[5th byte] to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		}
///////////////////////////////////////////////////////////////////////////	
	    i=7;
	    fieldvalues=input_int[7];
	    while(fieldvalues!=0)
		{
		   		checksum[i]=fieldvalues%2;           //Converting value of least significant byte[8th byte] to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		}
	    i=15;
	    fieldvalues=input_int[8];
	    while(fieldvalues!=0)
		{
		   		checksum[i]=fieldvalues%2;          //Converting value of  significant byte[7th byte] to its binary form
		   		i--;
		   		fieldvalues=fieldvalues/2;
		}    
/////////////////////////////////////////////////////////////////////////////		   
	    int []check=new int[16];
	    for(i=0;i<16;i++)
        {
        	check[i]=packet_type[i]^sequence_number[i]^no_of_packets[i]^payload[i]^checksum[i];     //Checksum for the entire packet
        }
	   int power=0,checksum_check=0;
	   for(i=15;i>=0;i--)
	   {
		   checksum_check=(int) (checksum_check+check[i]*Math.pow(2,power));                  // Getting single value of checksum result
		   power++;
	   }
	   if(checksum_check==0)                            // Check if checksum is 0; non-zero means there is error
	   return true;
	   else
		   return false;
	}
	public static int extractsequenceno(byte []input)   //Extracting sequence number from input
	{
		byte seq_1,seq_2;
        seq_1=input[1];                                 //1st byte of input 
        seq_2=input[2];                                 //2nd byte of second
        int seqnoholder1,seqnoholder2,i=0;              //initialization
        
        if(seq_1<0)                                     // IF value negative we need to add 256
        	seqnoholder1=seq_1+256;
        else
        	seqnoholder1=seq_1;
        
        if(seq_2<0)                                     // IF value negative we need to add 256
        	seqnoholder2=seq_2+256;
        else
        	seqnoholder2=seq_2;
        
        i=7;                                            //Counter for 8bits
        int []seq_no=new int[16];
        while(seqnoholder1!=0)
        {
         seq_no[i]=seqnoholder1%2;                     //Converting value of least significant byte[2nd byte] to its binary form
         seqnoholder1=seqnoholder1/2;
         i--;
        }
        i=15;
        while(seqnoholder2!=0)
        {
            seq_no[i]=seqnoholder2%2;                 //Converting value of least significant byte[1st byte] to its binary form
            seqnoholder2=seqnoholder2/2;
            i--;
        } 
         int power=0,sequence_number = 0;
        for(i=15;i>=0;i--)
        {
        	sequence_number=(int) (sequence_number+seq_no[i]*Math.pow(2,power));//Converting sequence number to bytes
        	power++;
        }
	   return sequence_number;
	}
}
