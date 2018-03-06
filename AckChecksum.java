
public class AckChecksum {
public AckChecksum(){
	
}
public static boolean integritycheck(byte[] Rx, int ori_seq_no) {
	
        int n,i;                                //Initialization
        int []input_int=new int[5];
	    for(i=0;i<5;i++)
	    {
	    	if(Rx[i]<0)                         //Check if the received number is negative 
	    	input_int[i]=Rx[i]+256;             
	    	else
	    	input_int[i]=Rx[i];
	    }
		   int []packet_type=new int[16]; 
		   n=input_int[0];i=15;                 //Initialization
		   while(n!=0)
	   	{
		   		packet_type[i]=n%2;            //Converting packet type to binary form
		   		i--;
		    		n=n/2;
		} 	
		 int []sequence_number=new int[16]; 
	     int []checksum=new int[16];
	     i=7;                                 //Counter to 8 bits
         n=input_int[1];                      //Copying the most significant byte of sequence number
	     while(n!=0)
		 {
		   		sequence_number[i]=n%2;       //Converting Sequence Number to binary form
		   		i--;
		   		n=n/2;
		 }
	     i=15;                                //Counter to 15 bits
         n=input_int[2];                      //Copying the least significant byte of sequence number
	     while(n!=0)
		 {
		   		sequence_number[i]=n%2;       //Converting Sequence Number to binary form
		   		i--;
		   		n=n/2;
		 }
         int new_seq_number=0;
         int power=0;
         for(i=15;i>=0;i--)
         {
         new_seq_number=(int) (new_seq_number+sequence_number[i]*Math.pow(2,power));//Sequence Number in Bytes
         power++;
         }
         for(i=15;i>=8;i--)
         {
	     sequence_number[i]=0;                //As no of packets are odd
         }
	     i=7;                                 //Counter to 7 bits
         n=input_int[3];                      //Copying values to a variable
	    while(n!=0)
		{
		   		checksum[i]=n%2;              //Converting to binary form 
		   		i--;
		   		n=n/2;
		}
	    i=15;
     n=input_int[4];
	    while(n!=0)
		{
		   		checksum[i]=n%2;             //Converting to Binary Form
		   		i--;
		   		n=n/2;
		} 	    
	    int []check=new int[16];
	    for(i=0;i<16;i++)
        {
     	check[i]=packet_type[i]^sequence_number[i]^checksum[i];// Ex-oring all the fields for integrity check value       
        }
	    power=0;
	    int checksum_check=0;
	    for(i=15;i>=0;i--)
	    {
		   checksum_check=(int) (checksum_check+check[i]*Math.pow(2,power));//Converting Checksum value to bytes
		   power++;
	    }
	   int flag = 0;
	   if((new_seq_number==ori_seq_no+1)||(new_seq_number==ori_seq_no))    //Check Condition for selecting IACK OR DACK checksum
	    flag=1;
	   if(flag==1&&checksum_check==0&&(input_int[0]==170||input_int[0]==204))
	   return true;
	   else
	   return false;
	} 
	public static int extractsequenceno(byte []input)
	{
		byte seq_1,seq_2;        //Initialization of variables for copying sequence Number
        seq_1=input[1];          //Copying the most significant byte to seq_1
        seq_2=input[2];          //Copying the least significant byte to seq_2
        int n,m,i=0;             //Initialization
        
        if(seq_1<0)              //Check if seq_1 has negative values if yes need to add with 256
        n=seq_1+256;
        else
        n=seq_1;
        
        if(seq_2<0)             //Check if seq_2 has negative values if yes need to add with 256
        m=seq_2+256;
        else
        m=seq_2;
        
       i=7;                    //Counter for 8 bits
        int []seq_no=new int[16];
        while(n!=0)
        {
         seq_no[i]=n%2;        //Converting most significant byte to binary form
         n=n/2;
         i--;
        }
        i=15;                  //Counter for 16 bits 
        while(m!=0)
        {
            seq_no[i]=m%2;     //Converting least significant byte to binary form
            m=m/2;
            i--;
        } 
         int power=0,sequence_number = 0;
        for(i=15;i>=0;i--)
        {
        	sequence_number=(int) (sequence_number+seq_no[i]*Math.pow(2,power));//Sequence Number binary to bytes
        	power++;
        }
	   return sequence_number;
	}
}