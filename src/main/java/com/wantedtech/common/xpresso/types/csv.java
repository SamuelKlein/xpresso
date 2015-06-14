package com.wantedtech.common.xpresso.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.io.IOException;
import java.io.StringWriter;

import com.wantedtech.common.xpresso.x;
import com.wantedtech.common.xpresso.csvs.CSVParser;
import com.wantedtech.common.xpresso.csvs.CSVWriter;

public class csv implements Iterable<list<String>>, AutoCloseable {

	HappyFile file;
	StringBuilder builder;
	
	String initializedFrom;
	
	public csv (HappyFile file) {
		this.file = file;
		initializedFrom = "file";
	}
	
	public csv (String path, String operation, String encoding) throws IOException  {
		if (x.String(operation).in(x.list("r", "w"))) {
			try {
				file = x.open(path, operation, encoding);
				initializedFrom = "file";
			} catch (Exception e) {
				throw new IOException("Could not open the file " + path + ".");
			}
		} else {
			throw new UnsupportedOperationException("Can only open csv files for \"r\" or \"w\" operations.");
		}

	}
	
	public csv (String path, String operation) throws IOException  {
		this(path, operation, "utf-8");
	}
	
	public csv (StringBuilder stringBuilder) {
		this.builder = stringBuilder;
		initializedFrom = "builder";
	}
	
	public void writerow(Iterable<?> iterable) throws IOException {
		List<String> iterAsList = new ArrayList<String>();
		for (Object element : iterable) {
			iterAsList.add(element.toString());
		}
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);
        String[] arr = new String[iterAsList.size()];
        arr = iterAsList.toArray(arr);
        writer.writeNext(arr);
		if (initializedFrom.equals("file") && file.getOperation().equals("w")) {
			file.writeLine(sw.toString().trim());
		} else if (initializedFrom.equals("builder")) {
			builder.append(sw.toString());
		} else {
			writer.close();
			throw new UnsupportedOperationException("You can only write into a StringBuilder object or a file open for writing in text mode.");
		}
		writer.close();
	}

	@Override
	public Iterator<list<String>> iterator() {
		final Iterator<String> fileIter = file.iterator();
		return new Iterator<list<String>>(){

			@Override
			public boolean hasNext() {
				try{
					return fileIter.hasNext();
				}catch(NullPointerException e) {
					return false;	
				}
			}

			@Override
			public list<String> next() {
				try{
					CSVParser parser = new CSVParser();
					return x.list(parser.parseLine(fileIter.next()));
				}catch(NullPointerException e) {
					throw new NoSuchElementException();
				}catch(IOException e2) {
					throw new NoSuchElementException();
				}
			}
		};
		
	}

	@Override
	public void close() throws Exception {
		try {
			file.close();		
		}catch (Exception e){
			
		}		
	}

}