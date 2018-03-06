import java.util.Arrays;
public class InitGenerate {
	public InitGenerate() {
		
	}
	public static byte[] initgenerate(int seq_no)
	{
		int n,i;                      //Initialization
		byte []init=new byte[9];      //Initialize byte array to store INIT Packet
		init[0]=85;                   //Packet type of INIT Packet 
		n=init[0];	                  //Copying packet type value to a variable 
		i=15;                         //Counter for 16 bits 
		int []packet_type=new int[16]; 
		while(n!=0)
		{
		   		packet_type[i]=n%2;   //Converting packet type byte to binary form
		   		i--;
		   		n=n/2;
		} 
		n=seq_no;                     //Copying sequence number to a variable
		i=15;                         //Counter for 16 bits
		int []sequence_number=new int[16]; 
		while(n!=0)
		{
		   		sequence_number[i]=n%2;//Converting Sequence number byte to binary form
		   		i--;
		   		n=n/2;
		} 
		int power=0;
		for(i=7;i>=0;i--)
		{
			init[1]=(byte) (init[1]+sequence_number[i]*Math.pow(2,power));//Sequence No to bytes
			power++;
		}
		power=0; 
		for(i=15;i>=8;i--)
		{
			init[2]=(byte) (init[2]+sequence_number[i]*Math.pow(2,power));//Sequence No to bytes
			power++;
		}
		init[3]=0;       //Initialization of value of field pertaining to number of data packets
		init[4]=10;      //Initialization of value of field pertaining to number of data packets
		n=init[4];	     //Copying values of number of data packets in a variable
		i=15;
		int []no_of_packets=new int[16]; 
		while(n!=0)
		{
		   		no_of_packets[i]=n%2;        //Convert number of packets into 16 bits
		   		i--;
		   		n=n/2;
		} 
		int pld=300;                        //Number of payload bytes to be transmitted
		n=pld;
		i=15;                               //Counter for 16 bits
		int []payload=new int[16];
		while(n!=0)
		{
		   		payload[i]=n%2;             //Converting the payload bytes to binary form
		   		i--;
		   		n=n/2;
		} 
		power=0;
		for(i=7;i>=0;i--)                  //Payload to bytes
		{
			init[5]=(byte) (init[5]+payload[i]*Math.pow(2,power));//Converting to bytes
			power++;
		}
		power=0;
		for(i=15;i>=8;i--)   
		{
			init[6]=(byte) (init[6]+payload[i]*Math.pow(2,power));//Converting to bytes
			power++;
		}	
		int []checksum=new int[16];
        for(i=15;i>=8;i--)                                        //No of bytes are odd in init packet
        {
        	payload[i]=0;                                         //As number of bytes are odd we have set last 8 bits to zero
        }
        for(i=0;i<16;i++)
        {
        	checksum[i]=packet_type[i]^sequence_number[i]^no_of_packets[i]^payload[i]; //EX-oring all the fields for integrity check
        }
        power=0;
		for(i=7;i>=0;i--)
		{
			init[7]=(byte) (init[7]+checksum[i]*Math.pow(2,power));//Integrity check to bytes
			power++;
		}
		power=0; 
		for(i=15;i>=8;i--)
		{
			init[8]=(byte) (init[8]+checksum[i]*Math.pow(2,power)); //Integrity check to bytes
			power++;
		}
		System.out.println("The bytes sent in INIT packet are");
		System.out.println(Arrays.toString(init));                 //Displaying INIT Packet
		return init;
	}
}
