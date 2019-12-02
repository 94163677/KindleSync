package air.kanna.kindlesync.util;

import java.util.Collection;

public class CollectionUtil {

	public static boolean isEmpty(Collection collection){
		if(collection == null || collection.size() <= 0){
			return true;
		}
		for(Object obj : collection){
			if(obj != null){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNotEmpty(Collection collection){
		return !isEmpty(collection);
	}
}
