package kr.re.ec.bibim.server.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import kr.re.ec.bibim.constants.Constants;
import kr.re.ec.bibim.server.da.NoteDataController;
import kr.re.ec.bibim.util.LogUtil;
import kr.re.ec.bibim.vo.NoteData;
import kr.re.ec.bibim.vowrapper.NoteDataWrapper;
import kr.re.ec.bibim.vowrapper.WrappedClassOpener;

public class NoteStream implements StreamLike{

	private static ServerSocket subserver;
	
	@Override
	public void Activate(StreamContext context) throws IOException,ClassNotFoundException{
		
		NoteDataWrapper ndw = new NoteDataWrapper();
		NoteData note = new NoteData();
		// create the socket server object
		subserver = new ServerSocket(Constants.NetworkConstantFrame.SUBPORT);
		// keep listens indefinitely until receives 'exit' call or program
		// terminates

		LogUtil.d("Waiting for client note request");
		// creating socket and waiting for client connection
		Socket socket = subserver.accept();

		// read from socket to ObjectInputStream object
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// convert ObjectInputStream object to String
		ndw = (NoteDataWrapper) ois.readObject();
		LogUtil.d("ndw is: " + ndw.getUserid() + "\t" + ndw.getTitle() + "\t"
				+ ndw.getFolderid());

		if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.INSERT)) {

			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.INSERT);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().insert(note);

		} else if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.SELECT)) {

			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.SELECT);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().findById(note.getNoteid());

		} else if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.DELETE)) {

			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.DELETE);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().deleteByID(note.getNoteid());

		} else if (ndw.getQueryHeader().equals(
				Constants.QueryHeaderConstantFrame.UPDATE)) {
			LogUtil.d("QueryHeader is "
					+ Constants.QueryHeaderConstantFrame.UPDATE);
			note = WrappedClassOpener.getInstance().OpenNoteDataWrapper(ndw);

			LogUtil.d("openedclass: " + note.getUserid() + "\t"
					+ note.getFolderid() + note.getTitle() + "\t"
					+ note.getContent() + "\t" + note.getDate());

			NoteDataController.getInstance().updateNote(note);
		}
		// create ObjectOutputStream object
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		// write object to Socket
		oos.writeObject(note);
		LogUtil.d("Message Sent: " + note.getNoteid());
		// close resources

		oos.close();
		ois.close();
		socket.close();
		// terminate the server if client sends exit request
		// close the ServerSocket object
		subserver.close();
		
	}
	
	

}
