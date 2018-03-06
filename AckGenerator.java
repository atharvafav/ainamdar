import java.util.Arrays;
public class AckGenerator {
	
public AckGenerator() {
	}
public static byte[] ackgenerate(int sequence_number,int selector)
{   
	int packet_type=0;                                            //initialization
	byte []ack=new byte[5];                                       //initialization
	if(selector==1) {                                             //If selector =1 then packet type value is for iack
	packet_type=170;                                              //Hexadecimal value aa in packet_type field.
	}
	else if(selector==2) {                                        //Else If selector =2 then packet type value is for iack
    packet_type=204;                                              //Hexadecimal value cc in packet_type field.
	}
	ack[0]=(byte)packet_type;
	int n=sequence_number;                                        //Variable used to store sequence number 
	int i=15;                                                     //Counter for 16 bits
	int []ack_number=new int[16]; 
	while(n!=0)
	{
	   		ack_number[i]=n%2;                                    //Converting sequence number in its binary form
	   		i--;
	   		n=n/2;
	} 
	int power=0;
	for(i=7;i>=0;i--)
	{
		ack[1]=(byte) (ack[1]+ack_number[i]*Math.pow(2,power));    //Sequence No to bytes
		power++;
	}
	power=0; 
	for(i=15;i>=8;i--)
	{
		ack[2]=(byte) (ack[2]+ack_number[i]*Math.pow(2,power));    //Sequence No to bytes
		power++;
	}
	////////////////////Computing the checksum for IACK bit/////////////////////////////
    int []checksum=new int[16];
    int []pack_type=new int[16];
    n=packet_type;i=15;
    while(n!=0)
    {
       pack_type[i]=n%2;
       n=n/2;
       i--;
    }
   ///////////////////////////////////////////////////////////////////////////////////
    for(i=15;i>=8;i--)                                             //No of bytes are odd in iack packet so least significant byte is zero
    {
    	ack_number[i]=0;                                           //As number of bytes are odd we have set last 8 bits to zero
    }
    for(i=0;i<16;i++)
    {
    	checksum[i]=pack_type[i]^ack_number[i];                    //Ex-oring all the fields to get integrity Check
    }
    power=0;
	for(i=7;i>=0;i--)
	{
		ack[3]=(byte) (ack[3]+checksum[i]*Math.pow(2,power));      //Integrity check to bytes
		power++;
	}
	power=0; 
	for(i=15;i>=8;i--)
	{
		ack[4]=(byte) (ack[4]+checksum[i]*Math.pow(2,power));      //Integrity check to bytes
		power++;
	}
	if(selector==1) {
	System.out.println("The bytes sent in IACK packet are");
	System.out.println(Arrays.toString(ack));                      //Display the IACK Packet
	}
	return ack;
}
}
