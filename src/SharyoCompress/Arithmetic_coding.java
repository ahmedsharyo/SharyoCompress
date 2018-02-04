package SharyoCompress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Arithmetic_coding {

	
	

	public static Double comp(String a) {

		int n = a.length();

	      HashMap<String,Integer > ind = new HashMap<String,Integer >();
	      String [] s=new String[n+1];
	      Double [] p=new Double[n+1];
	      Double [] c=new Double[n+1];
	      Double [] d=new Double[n+1];

			int cnt=1;
			for (int i = 0; i < n; i++) {
				String t = "";
				t += a.charAt(i);
				if (ind.containsKey(t)) {
					p[ind.get(t)]+=1.0;
					
				} else {
					ind.put(t,cnt);
					p[ind.get(t)]=1.0;
					cnt+=1;
				}
			}
		
			Double sum=0.0;
			for(int i=1;i<cnt;i++)
			{
				p[i]/=(n*1.0);
				c[i]=sum;
				sum+=p[i];
				d[i]=sum;
			}
			Double x=0.0 ,y=1.0,w;
			
			for (int i = 0; i < n; i++) {
				String t = "";
				t += a.charAt(i);
				int  j=ind.get(t);
				s[j]=t;
				w=y-x;
				y=x+w*d[j];
				x=x+w*c[j];
				
			}
			Double z=(x+y)/2.0;
			
			
			// write
			String path = "arithmetic_char.txt";
			try {
				FileOutputStream fos = new FileOutputStream(path);
				ObjectOutputStream os = new ObjectOutputStream(fos);
				
				for(int i=1;i<cnt;i++)
				{
					os.writeByte(s[i].charAt(0));
				}
				os.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			 path = "arithmetic_prob.txt";
			try {
				FileOutputStream fos = new FileOutputStream(path);
				ObjectOutputStream os = new ObjectOutputStream(fos);

				for(int i=1;i<cnt;i++)
				{
					os.writeDouble(p[i]);
				}
				os.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			 path = "arithmetic_code_and_length.txt";
			try {
				FileOutputStream fos = new FileOutputStream(path);
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeDouble(z);
				os.writeInt(n);

				os.close();
			} catch (FileNotFoundException e1) {
				String msg = "invalid path";
				e1.printStackTrace();
			} catch (IOException e1) {
				String msg = "an error occured";
				e1.printStackTrace();
			}
			
			
			return z;
		}
	
	
	public static String decomp() {
		
		  int n=0 ;
		  Double z=0.0;
		  
		  String path = "arithmetic_code_and_length.txt";

			
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream is = new ObjectInputStream(fis);
				z=is.readDouble();
				n=is.readInt();
				is.close();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	      String [] s=new String[n+1];
	      Double [] p=new Double[n+1];
	      Double [] c=new Double[n+1];
	      Double [] d=new Double[n+1];
		 path = "arithmetic_char.txt";
       
         int i=1;
		FileInputStream fis1;
		try {
			fis1 = new FileInputStream(path);
			ObjectInputStream is = new ObjectInputStream(fis1);
			while (is.available() != 0) {
				char input = (char) is.read();
				s[i++]=input+"";
			}
			is.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		 path = "arithmetic_prob.txt";
	       
          i=1;
		FileInputStream fis11;
		try {
			fis11 = new FileInputStream(path);
			ObjectInputStream is = new ObjectInputStream(fis11);
			while (is.available() != 0) {
				Double input =  is.readDouble();
				p[i++]=input;
			}
			is.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int cnt=i;
		Double sum=0.0;

		for( i=1;i<cnt;i++)
		{
			c[i]=sum;
			sum+=p[i];
			d[i]=sum;
		}
		Double x=0.0 ,y=1.0,w,x0=0.0 ,y0=0.0;
		String ret="";
		for(i=0;i<n;i++)
		{
			w=y-x;
			for(int j=1;j<cnt;j++)
			{
				 y0=x+w*d[j];
				 x0=x+w*c[j];
				if(x0<=z&&z<y0)
				{
					ret+=s[j];
					x=x0;
					y=y0;
					break;
				}
			}
		}
		return ret;
		
	}
	
}
