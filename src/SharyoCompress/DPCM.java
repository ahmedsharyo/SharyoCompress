package SharyoCompress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DPCM {

	public static void comp(int[][]b,int bits)
	{
		
		int n=b.length;
		int m=b[0].length;
		int a[]=new int[n*m];
		double d[]=new double[n*m];
		int c[]=new int[n*m];
		int a_sz=0;
		int d_sz=0;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				
			   a[a_sz++]=b[i][j];
			
			}
		}
		int lw=(int) 1e9;
		int up=(int) -1e9;
		for(int i=1;i<a_sz ;i++)
		{
			if(i%n==0)
				d[d_sz]=a[i]-a[i-n];
			else
			  d[d_sz]=a[i]-a[i-1];
			lw=(int) Math.min(lw,d[d_sz]);
			up=(int) Math.max(up, d[d_sz]);	
			d_sz++;	
		}
		double rang=up-lw+1;
		double levels= Math.pow(2, bits);
	    double step=rang/levels;

	   for(int j=1;j<a_sz;j++){
		   boolean v=false;
	    for(double i=lw,k=0;i<up;i+=step,k++)
	    {
	    	double l=i,r=i+step;
			
	    	j=(int)j;
	    	if(d[j]>=l&&d[j]<=r)
	    	{
	    		c[j]=(int) k;
	    		v=true;
	    		break;
	    	}	
	    	
	    }
	    if(!v)
	    {
	    	System.out.printf("erorr");
	    	
	    }
	    
	    }
	// write
			String path = "Image.txt";
			try {
				FileOutputStream fos = new FileOutputStream(path);
				ObjectOutputStream os = new ObjectOutputStream(fos);
				
				os.writeInt(n);
				os.writeInt(m);
				os.writeInt(lw);
				os.writeInt(up);
				os.writeDouble( step);
				os.writeInt(a[0]);
		    	System.out.printf("%d %d %d %d %f %d\n",n,m,lw,up,step,a[0]);

				for(int i=1;i<a_sz;i++)
				{
					os.writeInt(c[i]);
					System.out.printf("%d \n",c[i]);
				}

				os.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
		
	}
	public static int [][] decomp()
	{
		 String path = "Image.txt";

			int n=0,m=0,lw=0,up=0;
				double	step=0;
			int [] c=null;
			int Q[]=null;
         int []a=null;
         int [][]ret=null;
			
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream is = new ObjectInputStream(fis);
				n=is.readInt();
				m=is.readInt();
				lw=is.readInt();
				up=is.readInt();
				step=is.readDouble();
			   a=new int[n*m];
		     	a[0]=is.readInt();
		      ret= new int [n][m];	
		       Q=new int [n*m];
		       c=new int [n*m];
			 for(int i=1;i<(n*m);i++)
			 {
				 c[i]=is.readInt();
			 }
				is.close();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int k=0;
			 for(double i=lw;i<up;i+=step)
			    {
				 double l=i,r=i+step;
			    	Q[k++]=(int)Math.round((l+r)/2.0);
			    }
			 
			for(int i=1;i<n*m;i++)
			{
				if(i%n==0)
				a[i]=Q[c[i]]+a[i-n];
				else
				a[i]=Q[c[i]]+a[i-1];
			}
			int cnt=0;
		    for(int i=0;i<n;i++)
		    {
		    	for(int j=0;j<m;j++)
		    	{
		    		ret[i][j]=a[cnt++];
		    	}
		    }
			
		return ret;
		
	}
}
