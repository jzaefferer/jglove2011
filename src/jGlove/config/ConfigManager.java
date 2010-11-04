package jGlove.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * mangages different types of configurations, eg. presets and user configs
 * 
 * usage: You need to provide at least a label for every config.
 * <code>
 * addConfig("Label of this config", new Config());
 * </code>
 * 
 * in addition, you can provide information like description, help text
 * <code>
 * String details[] = {"Label", "Description", "Help text"};
 * addConfig(details, new Config());
 * </code>
 * 
 * usually, you want to use getConfigLabels() to get all labels, display those
 * in a appropirate widget (i.e. dropdown menu) and then use the index of the
 * selected element in a getXyz() method
 * 
 * example:
 * String[] labels = cm.getConfigLabels();
 * int selection = assignToWidgetAndGetSelectionIndexMethod(labels);
 * Config c = cm.getConfig(selection);
 * 
 * use load() and save() to store configs and load them
 * cm = new ConfigManager();
 * cm.addConfig("Label", new Config());
 * cm.save();
 * // after program restart: cm = new ConfigManager(); cm.load();
 * cm.getConfig(0);
 * 
 */
public class ConfigManager {

	/**
	 * stream-variable for persistent-writting of objects
	 */
	private ObjectOutputStream objectOutputStream;

	/**
	 * stream-variable for reading of persistent-written objects 
	 */
	private ObjectInputStream objectInputStream;

	/**
	 * name of the file, in which the data should be storaged
	 */
	private File workfile;

	/**
	 * saves the configuration
	 */
	private ArrayList config;

	/**
	 * saves all given labels and/or details
	 */
	private ArrayList labels;

	/**
	 * contains error message
	 */
	private String message;
	
	/**
	 * constructor
	 * 
	 * @param filename storage for all configs
	 */
	public ConfigManager(String filename) {
		this.workfile = new File(filename);
	}
	
	/**
	 * constructor
	 * 
	 * @param file storage for all configs
	 */
	public ConfigManager(File file) {
		if(file == null) {
			throw new IllegalArgumentException("file can not be null"); //$NON-NLS-1$
		}
		this.workfile = file;
	}
	
	private void init() {
		if(config == null) {
			config = new ArrayList(4);
		}
		if(labels == null) {
			labels = new ArrayList(4);
		}
	}

	/**
	 * adds a configuration with a label
	 * 
	 * @param label
	 * @param c
	 */
	public void add(String label, Config c) {
		init();
		String[] lab = { label };
		if (label != null && c != null) {
			labels.add(lab);
			config.add(c);
		}
	}

	/**
	 * adds a configuration with additional information like label, description
	 * 
	 * @param details
	 * @param c
	 */
	public void add(String[] details, Config c) {
		init();
		if (details != null && c != null) {
			labels.add(details);
			config.add(c);
		}
	}

	/**
	 * remove a config from the given index
	 * 
	 * @param index
	 */
	public void remove(int index) throws NullPointerException {
		if(config == null || config.size() == 0 || labels == null || labels.size() == 0 ) {
			throw new NullPointerException("no init yet or no elements to remove"); //$NON-NLS-1$
		}
		labels.remove(index);
		config.remove(index);
	}

    /**
     * returns the number of available configurations
     * @return the number of configs loaded and/or added
     */
	public int getCount() {
		return config.size();
	}

	/**
	 * returns the labels of all available configs
	 * 
	 * @return the labels or null, if no labels are found
	 */
	public String[] getLabels() {
		Object[] array = labels.toArray();
		String[] ret = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			Object[] zeile = (Object[]) array[i];
			ret[i] = (String) zeile[0];
		}
		if(ret.length == 0) {
			return null;
		}
		return ret;
	}

	/**
	 * returns labels, description etc. of all available configs
	 * 
	 * @return details as String array or null, if none is found
	 */
	public String[][] getDetails() {
		Object[] array = labels.toArray();
		String[][] ret = new String[array.length][];
		for (int i = 0; i < array.length; i++) {
			Object[] zeile = (Object[]) array[i];
			ret[i] = (String[]) zeile;
		}
		if(ret.length == 0) {
			return null;
		}
		return ret;
	}

	/**
	 * 
	 * @param index the index of the config      
	 * @return a config object
	 */
	public Config get(int index) {
		return (Config) config.get(index);
	}

	/**
	 * writes all configs to disk
	 */
	public void save() {
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(workfile));
			
			Hashtable objects = new Hashtable();
			objects.put("Config",config); //$NON-NLS-1$
			objects.put("Labels",labels); //$NON-NLS-1$
			
			objectOutputStream.writeObject(objects);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads all configs from disk
	 * 
	 * @return number of configs loaded, 0 on error (use getError() to get error message)
	 */
	public int load() {
		if(labels != null || config != null) {
			setError("can not load, did already add or load configs"); //$NON-NLS-1$
			return 0;
		}
		init();
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(workfile));			
			Hashtable objects2 = (Hashtable)objectInputStream.readObject();
			objectInputStream.close();
			Enumeration names = objects2.keys();
			while(names.hasMoreElements()) {
				// Holt das Objekt mit dem spezifischen Namen
				String name = (String)names.nextElement();
				Object obj = objects2.get(name);
				if(name.equals("Config")) { //$NON-NLS-1$
					config = (ArrayList)obj;
				} else if(name.equals("Labels")) { //$NON-NLS-1$
					labels = (ArrayList)obj;
				}
			}
			return config.size();
		} catch (IOException e) {
			setError(e.getMessage());
			return 0;
		} catch (ClassNotFoundException e) {
			setError(e.getMessage());
			return 0;
		}
	}
	
	public String getError() {
		return message;
	}
	
	private void setError(String message) {
		this.message = message;
	}
	
}
