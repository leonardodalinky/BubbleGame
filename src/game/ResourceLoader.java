package game;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;
import java.awt.*;
import java.util.*;

public class ResourceLoader {
	
	private HashMap<String, Image> rscImMap = new HashMap<String, Image>();
	private static final int read_thread_size = 3;
	private static final String pathr = new String("./pic/");
	private static final String path1 = new String("character");
	private static final String path2 = new String("item");
	private static final String path3 = new String("UI");
	
	public ResourceLoader() {
		
	}

	public void init() {
		ReadImageThread[] ts = new ReadImageThread[read_thread_size];
		ts[0] = new ReadImageThread(rscImMap, pathr + path1);
		ts[1] = new ReadImageThread(rscImMap, pathr + path2);
		ts[2] = new ReadImageThread(rscImMap, pathr + path3);
		
		for (Thread t : ts) {
			t.start();
		}
		for (Thread t : ts) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param srcName the filename of the source image
	 */
	public Image getResource(String srcName) {
		return rscImMap.get(srcName.toLowerCase());
	}

	private class ReadImageThread extends Thread {
		
		private HashMap<String, Image> rscImMap;
		private String pathDir;
		
		public ReadImageThread(HashMap<String, Image> rscImMap, String pathDir) {
			this.pathDir = pathDir;
			this.rscImMap = rscImMap;
		}
		
		@Override
		public void run() {
			File dir = new File(pathDir);
			if (!dir.isDirectory()) return;
			File [] files = dir.listFiles();
			for (File file : files) {
				String fullname = file.getName();
				int lastdotIndex = fullname.lastIndexOf('.');
				if (lastdotIndex == -1) continue;
				String preName = fullname.substring(0, lastdotIndex).toLowerCase();
				String sufName = fullname.substring(lastdotIndex + 1).toLowerCase();
				if (!sufName.equals("jpg") && !sufName.equals("jpeg") && !sufName.equals("png") && !sufName.equals("bmp"))
					continue;
				ImageIcon img = new ImageIcon(file.getAbsolutePath());
				synchronized (rscImMap) {
					rscImMap.put(preName, img.getImage());
				}
			}
		}
	}
}