package ltg.ns.comparators;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import ltg.ns.ambient.model.Note;

public class NoteComparator implements Comparator<Note>{
	final String format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	final DateFormat formatter = new SimpleDateFormat(format);
	@Override
	public int compare(Note o1, Note o2) {
		String date1string = o1.getCreated_at();
		String date2string = o2.getCreated_at();
		Date date1;
		Date date2;
		try {
			date1 = formatter.parse(date1string);
			date2 = formatter.parse(date2string);
			if(date1.compareTo(date2)>0){
        		return 1;
        	}else if(date1.compareTo(date2)<0){
        		return -1;
        	}else if(date1.compareTo(date2)==0){
        		return 0;
        	}else{
        		System.out.println("How to get here?");
        	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	

}
