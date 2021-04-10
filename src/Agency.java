import java.util.*;
import java.util.List;
import java.util.stream.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.file.*;

public final class Agency implements Serializable {
	private static final long serialVersionUID = 3796183982203388632L;	
	
	public Agency() {
		new File("./Agency").mkdir();
		new File("./Agency/Clients").mkdir();
		new File("./Agency/Images").mkdir();
	}
	
	public void addClient(int Cin, String Name) {
		Client NewClient = new Client(Cin, Name);
		try {
			//Saving of object in a file 
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./Agency/Clients/" + NewClient.getCIN() + ".txt"));
			// Method for serialization of object
			oos.writeObject(NewClient);
			
			oos.flush();
			oos.close();
		}
		
		catch(IOException ex) { 
            System.out.println("IOException is caught :" + ex); 
        }
	}
	
	public void Upload_Image(ImageIcon image, int CIN) {
		BufferedImage imagebuf = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics grph = imagebuf.createGraphics();
		// paint the Icon to the BufferedImage.
		image.paintIcon(null, grph, 0, 0);
		grph.dispose();
	    try {
	        ImageIO.write(imagebuf, "jpg", new File("./Agency/Images/" + CIN + ".jpg"));
	    }
	    catch (Exception e) {
	        System.out.println("error :" + e);
	    }
	}
	
	public List<Client> ClientsList() {
		List<Client> list_Clients = new ArrayList<>();
		
		Stream<Path> filePathStream = null;
		try {
			filePathStream = Files.walk(Paths.get("./Agency/Clients"));
		}
		catch (IOException e) {
			System.out.println("IOException is caught :" + e); 
			e.printStackTrace();
		}
	    filePathStream.forEach(filePath -> {
	        if (Files.isRegularFile(filePath)) {
	        	try {
		            // Reading the object from a file 
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toString()));
		            // Method for deserialization of object 
					Client Cl = (Client) ois.readObject();
					ois.close();
					list_Clients.add(Cl);
	        	}
	        	catch(IOException ex) { 
		            System.out.println("IOException is caught :" + ex); 
		        } 
		          
		        catch(ClassNotFoundException ex) { 
		            System.out.println("ClassNotFoundException is caught :" + ex);
		        }
	        }
	    });
	    filePathStream.close();
	    return list_Clients;
	}
	
	public Client SearchClient(int ClientCIN){
		List<Client> list = ClientsList();
		for (int i = 0; i < list.size(); i++) {
			Client Cl = list.get(i);
			if (Cl.getCIN() == ClientCIN) {
				return Cl;
			}
		}
	    return null;
	}
	public boolean removeClient(int ClientCIN) {
		if ( (new File("./Agency/Clients/" + ClientCIN + ".txt").delete()) &&
				(new File("./Agency/Images/" + ClientCIN + ".jpg").delete()) ){
			return true;
		}
		return false;
	}
}
