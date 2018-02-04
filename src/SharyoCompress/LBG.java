package SharyoCompress;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;



public class LBG {
	
	public static class Globals {
		   public   static int c = 0;
		   public   static int B=0;
		   public   static Double x=0.0;
		   public   static int N=0;
		   public   static int q[]= new int [N];
	 }
   
	
	public static void Quantize(int i,int j,int lev,int []a)
	{
		int sum=0,av,r=i;
		
		for(int k=i;k<=j;k++)
		{
			sum+=a[k];
		}
		av=sum/(j-i+1);
		if(lev==Globals.B)
		{
			Globals.q[Globals.c++]=av;
			return;
		}
		for(int k=i;k<=j;k++)
		{
			if(a[k]<=av)
			{
				r=k;
			}else break;
		}
		Quantize(i,r,lev+1,a);
		Quantize(Math.min(r+1,j),j,lev+1,a);
		
	}
	
	
	public static void comp(int[][]b)
	{
		Globals.x=Math.pow(2.0,Globals.B+0.0);
		Globals.N=Globals.x.intValue();
		Globals.q= new int [Globals.N];
		
		int n=b.length;
		int m=b[0].length;
	int [] a=new int[n*m];
	int [] Q=new int[n*m];

	int k=0;
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++)
		{
			a[k++]=b[i][j];
		}
	}

	Arrays.sort(a);
	Quantize(0,k-1,0,a);
	int l=0,r=0;
	int [] L=new int[Globals.c];
	int [] R=new int[Globals.c];

		for(int j=0;j<Globals.c;j++)
		{
			if(j==Globals.c-1)r=10000;
			else r=(Globals.q[j]+Globals.q[j+1])/2;
			L[j]=l;
			R[j]=r;
			l=r+1;
		}
		int I=0;
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<m;j++)
			{
				a[I]=b[i][j];
				for(int K=0;K<Globals.c;K++)
				{
					if(a[I]>=L[K]&&a[I]<=R[K])
					{
						Q[I++]=K;
						break;
						
					}
				}
			}
		}
		
		
			
		
		// write
		String path = "Image.txt";
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			
			os.writeInt(n);
			os.writeInt(m);
			os.writeInt(Globals.c);
			
			for(int j=0;j<Globals.c;j++)
			{
				os.writeInt(Globals.q[j]);
			}
			
			for(int i=0;i<k;i++)
			{				
				os.writeInt(Q[i]);
			}
			
			os.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	
	}
	public static int [][] decomp()
	{
	
		 String path = "Image.txt";

			int n=0,m=0,c=0;
			int [] Q=null;
            int [][]a=null;
			
			FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				ObjectInputStream is = new ObjectInputStream(fis);
				n=is.readInt();
				m=is.readInt();
				c=is.readInt();

				Q=new int[c];
	            a=new int [n][m];
				for(int j=0;j<c;j++)
				{
					Q[j]=is.readInt();
				}
				for(int i=0;i<n;i++)
				{
					for(int j=0;j<m;j++)
					{
						a[i][j]=is.readInt();
					}
				}
				is.close();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int i=0;i<n;i++)
			{
				for(int j=0;j<m;j++)
				{
				a[i][j]=Q[a[i][j]];
					
				}
			}
			
			return a;
			
			
	}
	
	
}
