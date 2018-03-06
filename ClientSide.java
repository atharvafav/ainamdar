import java.util.*;
import java.io.*;
import java.net.*;
public class ClientSide {
        public static void main(String[] args) {
        	 Random random=new Random();
             int seq_no=random.nextInt(65535);                   //Random Sequence Number Generated
        	 System.out.println("Sequence_Number "+seq_no);
             InitGenerate in=new InitGenerate();
             AckChecksum check=new AckChecksum();
     	     byte []init=new byte[9]; byte []Rx=new byte[5];         //initialization
 		     long t1=0,t2=0, t3=0; float sum=0;                      //initialization
 	         int i,j,timeout=1000;                                   //initialization
 	         init=in.initgenerate(seq_no);                           //Generate INIT packet for the random sequence number
 	       try {
 	              DatagramSocket tx=new DatagramSocket();
			      InetAddress h=InetAddress.getByName("localhost"); //Destination IP address
			      int sSocket=9990;
			      DatagramPacket transmission=new DatagramPacket(init,init.length,h,sSocket);
			      DatagramPacket reception=new DatagramPacket(Rx,Rx.length);
				for(i=0;i<4;i++)
				{	
 	              try {
 	            	  tx.send(transmission);                             //Sending packets/bytes of INIT packet
 	            	  tx.setSoTimeout(timeout);                          //Set timer
 	            	  System.out.println("INIT sent to the server.");
 	    		      System.out.println("Waiting for the IACK........");
 	    		      tx.receive(reception);                             //IACK reception
 	    		      System.out.println("The bytes received in the IACK packet are");
 	    	          System.out.println(Arrays.toString(Rx));
 	    		      boolean iackcheck=check.integritycheck(Rx,seq_no); //Check integrity of received IACK packet
 	                  if(iackcheck)                                      //If iack integrity check is successful then true
 	                  {
 	                	 System.out.println("Integrity check passed for IACK packet");
 	      		         System.out.println("Connection established for data transfer");
 	                	  break;
 	                  }
 	                  else
 	                  {
 	                	 if(timeout>=8000)                                //If time greater than 8000 then print connection faliure
 	 	            	 {
 	 	            		 System.out.println("Connection Failure");   
 	 	            		 System.exit(0);
 	 	            	 }
 	                	  System.out.println("Integrity check failed for INIT packet");
 	                	  System.out.println("INIT packet is re-transmitted");
 	                	  timeout=timeout*2;   //increment the time for each failed iackcheck
 	                  }
 	              }
 	              catch(SocketTimeoutException e)                         //Exception handling
 	              {
 	            	 if(timeout>=8000)
 	            	 {
 	            		 System.out.println("Connection Failure");
 	            		 System.exit(0);
 	            	 }
 	            	 System.out.println("Time-out");
 	            	 System.out.println("INIT packet is re-transmitted");
 	            	 timeout=timeout*2;   //increment the time for each timeout
 	              }
				}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////Further code is executed only when INIT is sent and correct IACK is received//////////////////////////
				DataGenerate dg=new DataGenerate();
			     seq_no=seq_no+1;               //Sequence number incremented
			     byte []data=new byte[305];
			     byte []dack=new byte[5];
			     i=1;timeout=1000;
			   while(i<=10)  //Sending 10 data packets
			    {
			     data=dg.datagenerate(seq_no);  //Generating data packet 
			     seq_no=seq_no+300;             //Incrementing sequence number by 300 for the 300 byte data
				  if(seq_no>=65536) seq_no=seq_no-65535; //Round-off sequence number if it is greater than 16 bit.
			     for(j=0;j<4;j++)           //Loop of 4--[1 for normal transmission of data and 3 for re-transmission incase of failure
			     {
			      try {		    	  
			           /* if(i==7)
			             {
			            	 if(j==0||j==1) //[BUG 1]...The 7th data packet is corrupted and then correct data packet is sent during the 2rd re-transmission
			            		 data[0]=0;
			            	 else
			            	   data[0]=51;
			             }*/
			    	  DatagramPacket transmission1=new DatagramPacket(data,data.length,h,sSocket);
					     DatagramPacket reception1=new DatagramPacket(dack,dack.length);
					     tx.send(transmission1);                              //Sending DATA Packet.
					     t1=System.currentTimeMillis();                       //Setting the timer t1
			      tx.setSoTimeout(timeout);
			      System.out.println("The bytes sent in the DATA packet "+i+" are");
				  System.out.println(Arrays.toString(data));
				  tx.receive(reception1);                                     //DACK reception
				  t2=System.currentTimeMillis();                              //Setting timer t2
				  System.out.println("The bytes received in DACK packet "+i+" are ");
				  System.out.println(Arrays.toString(dack));	  
				  if(check.integritycheck(dack, seq_no))                      //DACK Integrity Check
				  { 
				  System.out.println("Integrity check passed for DACK packet "+i);
				  t3=t2-t1;          //Calculating the time between the start of data transmission to the last data byte
				  sum=sum+t3;
				  seq_no=check.extractsequenceno(dack);                       //extracting sequence Number
				  i++;
				  break;
				  }
				  else {
					  System.out.println("Integrity check failed for DACK packet "+i);
					  if(timeout>=8000)
	 	            	 {
	 	            		 System.out.println("Data transfer failure");
	 	            		 System.exit(0);
	 	            	 }
	                	 
	                	  System.out.println("Data packet "+i+" is re-transmitted");
	                	  timeout=timeout*2;                   //Incrementing the time if integrity check failed
                       }
			     }
			      catch(SocketTimeoutException f) {
			    	  System.out.println("Timed out");
			    	  if(timeout>=8000)
	 	            	 {
	 	            		 System.out.println("Data transfer failure");
	 	            		 System.exit(0);
	 	            	 }			    	      
			    	      System.out.println("Data packet "+i+" is re-transmitted");
	 	            	  timeout=timeout*2;               //Incrementing the time if timeout
			      }
			    }
			  } 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////			   
////////////////////////////////////Displaying the Data transfer characteristics/////////////////////////////////			   
			   sum=sum/10;
			     System.out.println("//////////////////////////////////////////////////////////////////////");
			     System.out.println("//////////////////Data transfer characteristics///////////////////////");
			     System.out.printf("The average round-trip time is %.2f ms",sum);
 	       }
 	       catch(Exception z)
 	       {
 	    	   System.out.println(z.getMessage());
 	    	   System.exit(0);
 	       }
 	 }
}