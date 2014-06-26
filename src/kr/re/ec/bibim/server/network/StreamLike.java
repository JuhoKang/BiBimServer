package kr.re.ec.bibim.server.network;

import java.io.IOException;

interface StreamLike {

	//should find how to delete exceptions
	public void Activate(StreamContext context) throws IOException, ClassNotFoundException;
	
}
