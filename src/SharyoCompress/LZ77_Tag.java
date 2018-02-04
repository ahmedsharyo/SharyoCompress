package SharyoCompress;

public  class LZ77_Tag {
	
    int last,len;
    char nxt;
    LZ77_Tag (int Last ,int Len,char Nxt)
    {
    	last=Last;
    	len=Len;
        nxt=Nxt;
    }
}
