import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
public class ServerSide {		
     public static void main(String[] args) {	
    	 InitChecksum in=new InitChecksum();
    	 AckGenerator ackg=new AckGenerator();
    	 DataChecksum dc=new DataChecksum();
 		try
         {
 			int i=0,count=1,x=1;                                                 //initialization
 		    long total_time = 0,numberofbytes=0;                                 //initialization
 		    int seq_no=0;
 			DatagramSocket rx=new DatagramSocket(9990);                          //Port number
 		    System.out.println("Waiting for a message from the client.......");
 			byte []input=new byte[305];                                          
 			byte []prev=new byte[5];
 			for(;;) 	{
 				if(count==11)                                                    //Condition to terminate the code once the data transfer of 10 packets is complete
 				{
 					System.out.println("Data Transfer Complete");
 					break;
 				}
 				DatagramPacket reception=new DatagramPacket(input,input.length);
 				rx.receive(reception);                                           //Packet is received
 				int sequence_number=in.extractsequenceno(input);                 //Extracting sequence number of the  packet 
 			if(in.initintegritycheck(input)&&input[0]==85)                       //Integrity check for INIT packet
 			{
 				System.out.println("The received INIT packet is");
 				byte []init=Arrays.copyOf(input,9);
 				System.out.println(Arrays.toString(init));                       //Display INIT Packet
 				System.out.println("Integrity check passed for INIT packet");
 				sequence_number=sequence_number+1;
 				i=1;                                                             //set as 1 to select iACK in AckGenerator class
 				byte []iack=ackg.ackgenerate(sequence_number,i);                 //Generate IACK packet using generic AckGenerator class
 				DatagramPacket transmission=new DatagramPacket(iack,iack.length,reception.getAddress(),reception.getPort());
 				rx.send(transmission);                                           //Transmit IACK Packet.
 	            System.out.println("IACK sent to the client.");
 	            seq_no=in.extractsequenceno(input)+1;                            //Extracting sequence number from the received input
 	            prev=iack;
 			}
 			else if(dc.dataintegritycheck(input)&&input[0]==51)                 //For Data, Integrity check and packet Type check
 			{
 				if(count==1||count==10)                                         //Select timer and get the overall time-between first data packet to last data packet
 			{
 				long t=System.currentTimeMillis();
 				total_time=t-total_time;                                        //Total time between first data packet and last data packet
 			}
 				System.out.println("The Received Data packet is");
 				System.out.println(Arrays.toString(input));
 				byte []dack=new byte[5];
 				numberofbytes=numberofbytes+input.length;
 			    if(sequence_number==seq_no)                                     //Check if sequence number matches with the previous DACK OR IACK
 			    {
 			    	System.out.println("Integrity check passed for Data Packet ");
 			    	seq_no=seq_no+300;
 	                if(seq_no>=65536)
 	                seq_no=seq_no-65536;		
 	                i=2;
 	                dack=ackg.ackgenerate(seq_no,i);  
 	             /*  if(count==7)  //[BUG_2]-----Introducing bug--IF corrupt dack for Packet7 is send then client should re-transmit----
 	            	  dack[0]=0;    
 	               else 
 	               {
 	            	   dack[0]=-52;
 	            	//Generate DACK Packet
 	               }*/
 	                prev=dack; 
 	             DatagramPacket transmission1=new DatagramPacket(dack,dack.length,reception.getAddress(),reception.getPort());
     	     	 rx.send(transmission1);                                        //Transmit DACK Packet
     	     	 System.out.println("The bytes sent in DACK packet are");
      	 		 System.out.println(Arrays.toString(dack));
                 count++;
 			    }
 			    else if(sequence_number==(seq_no-300))                         //If packet re-transmitted/Duplicate Packet
 			    {
 			    	/*if(x==2)     //[BUG_2]----we are sending corrupt dack for only 1 re-transmmsion , for second re-transmmsion the dack is corrected 
 			        prev[0]=-52;*/
 			    	DatagramPacket transmission2=new DatagramPacket(prev,prev.length,reception.getAddress(),reception.getPort());
 	     	     	rx.send(transmission2);                                    //Transmit the previous Acknowledged DACK
 	     	     	System.out.println("The bytes sent in DACK packet are");
 	      	 		System.out.println(Arrays.toString(prev));                 // Display previous DACK when duplicate packet is send
 	      	 		x++;
 			    }
 			    else
 			    {
 			    	System.out.println("Integrity check failed for Data Packet");
 			    }
 			 }
 			else
 			{
 				if(input.length>=305)
 				numberofbytes=numberofbytes+input.length;
 				System.out.println("Packet not received correctly");                  //For Integrity Failure
 			}i=0;
 			}
 		System.out.println("////////////////////////////////////////////////////////////////////");
 		System.out.println("////////////////////Data transfer chracteristics////////////////////");
 		float datarate=(float)(numberofbytes*8*1000)/(total_time*1000000);      //Converting to MBiyts/sec
 		System.out.printf("The effective data rate is %.5f Mbits/s",datarate);  //Displaying complete Data rate of data transfer
         }	
 		catch(Exception z)                                                      //Exception handling
		{
	    	 System.out.println(z.toString());
			 System.exit(0);
    }  
  }
}