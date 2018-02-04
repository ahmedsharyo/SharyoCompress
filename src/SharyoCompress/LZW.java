package SharyoCompress;

import java.util.Vector;

public class LZW {


	static Vector<String> intialize() {
		Vector<String> Dect = new Vector<String>();
		for (char i = 0; i < 128; i++) {
			String tmp = "" + String.valueOf(i);
			Dect.addElement(tmp);
		}
		return Dect;

	}

	public static Vector<Integer> comp(String a) {
		Vector<String> Dect = new Vector<String>();
		Vector<Integer> ret = new Vector<>();
		Dect = intialize();
		int n = a.length();
		String cur;
		int en;
		for (int i = 0; i < n; i++) {
			cur = "" + a.charAt(i);
			en = n;
			for (int j = i + 1; j < n; j++) {
				cur += a.charAt(j);
				if (Dect.indexOf(cur) == -1) {
					en = j;
					break;
				}
			}
			String found = a.substring(i, en);
			int f = Dect.indexOf(found);
			ret.addElement(f);

			if (en != n)
				Dect.addElement(cur);

			i = en - 1;
		}

		return ret;
	}

	
	public static String decomp(Vector<Integer> b) {
		Vector<String> Dect = new Vector<String>();
		Dect = intialize();
		int n = b.size();
		String ret = "",p="",c="",pws="",cws="";
		int pw=-1,cw=0;
		for (int i = 0; i < n; i++) {
			if(pw!=-1)
				pws=Dect.get(pw);
			cw=b.get(i);
			if(cw<Dect.size())
			{
				
			cws= Dect.get(cw);
			ret+=cws;
			p=pws;
			c=""+cws.charAt(0);
			}
			else 
			{
				p=pws;  
				c=""+pws.charAt(0);
				ret+=p+c;
			}
			if(Dect.indexOf(p+c)==-1)
			   Dect.addElement(p+c);	
			pw=cw;
		}
		return ret;
	}

}
