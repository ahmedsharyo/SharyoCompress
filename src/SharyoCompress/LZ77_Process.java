package SharyoCompress;

import java.util.*;

public class LZ77_Process {

	public static Vector<LZ77_Tag> comp(String a) {
		Vector<LZ77_Tag> tags = new Vector<LZ77_Tag>();
		int i = 0;
		String cur = "";
		int n = a.length();
		LZ77_Tag tmp = new LZ77_Tag(0, 0, 't');
		boolean prefix = false;

		while (true) {
			
			if (i == n - 1) {
				
				if (!prefix)
					tmp = new LZ77_Tag(0, 0, a.charAt(i));
				
				tags.addElement(tmp);
				break;
			}

			cur += a.charAt(i);

			int f = find(0, i - 1, cur, a);

			if (f == -1) {

				if (!prefix) {
					tmp.last = 0;
					tmp.len = 0;
					tmp.nxt = a.charAt(i);
				}
				tags.addElement(tmp);
				cur = "";

				tmp = new LZ77_Tag(0, 0, 'a');
				prefix = false;
			} else {
				prefix = true;
				tmp.last = i - (cur.length() - 1) - f;
				tmp.len = cur.length();
				tmp.nxt = a.charAt(i + 1);
			}
			i++;

		}
		
		return tags;
	}

	public static int find(int r, int l, String cur, String a) {
		if (l < r)
			return -1;
		String tmp = a.substring(r, l + 1);
		return tmp.lastIndexOf(cur);
	}

	public static String decomp(Vector<LZ77_Tag> tags) {
		String a = "";
		for (LZ77_Tag x : tags) {

			if (x.len != 0) {
				if (x.last <= x.len)		
				{
					for (int i = 0; i < x.len / x.last; i++)
						a += a.substring(a.length() - x.last);
					int mod = x.len % x.last;
					String cycle = a.substring(a.length() - x.last);
					a += cycle.substring(0, mod);
				} else {
					int i = a.length() - x.last;

					a += a.substring(i, i + x.len);
				}
			}
			a += x.nxt;
		}

		return a;
	}

}
