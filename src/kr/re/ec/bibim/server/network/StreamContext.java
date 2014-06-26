package kr.re.ec.bibim.server.network;

import java.io.IOException;

public class StreamContext {
	
	private StreamLike mystream;
	public StreamContext() {
		
	}
	
	void setStream(final StreamLike newstream){
		mystream = newstream;
	}
	
	public void Activate() throws IOException, ClassNotFoundException{
		
			mystream.Activate(this);
	}
}
