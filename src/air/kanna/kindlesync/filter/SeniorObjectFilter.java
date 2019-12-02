package air.kanna.kindlesync.filter;

import java.util.ArrayList;
import java.util.List;

import air.kanna.kindlesync.util.CollectionUtil;

public abstract class SeniorObjectFilter<T> implements ObjectFilter<T> {

	protected List<ObjectFilter<T>> acceptAllFilters;
	protected List<ObjectFilter<T>> acceptOneFilters;
	protected List<ObjectFilter<T>> acceptNotFilters;
	
	protected SeniorObjectFilter(){
		acceptAllFilters = new ArrayList<>();
		acceptOneFilters = new ArrayList<>();
		acceptNotFilters = new ArrayList<>();
	}
	
	@Override
	public boolean accept(T object){
		if(object == null){
			return false;
		}
		
		if(!acceptAll(object)){
			return false;
		}
		
		if(!acceptOne(object)){
			return false;
		}
		
		return acceptNot(object);
	}
	
	protected boolean acceptAll(T object){
		for(ObjectFilter<T> allFilter : acceptAllFilters){
			if(!allFilter.accept(object)){
				return false;
			}
		}
		return true;
	}
	
	protected boolean acceptOne(T object){
		if(CollectionUtil.isEmpty(acceptOneFilters)){
			return true;
		}
		for(ObjectFilter<T> oneFilter : acceptOneFilters){
			if(oneFilter.accept(object)){
				return true;
			}
		}
		return false;
	}
	
	protected boolean acceptNot(T object){
		for(ObjectFilter<T> notFilter : acceptNotFilters){
			if(notFilter.accept(object)){
				return false;
			}
		}
		return true;
	}
	
	public List<ObjectFilter<T>> getAcceptAllFilters() {
		return acceptAllFilters;
	}
	public void setAcceptAllFilters(List<ObjectFilter<T>> acceptAllFilters) {
		this.acceptAllFilters.clear();
		if(CollectionUtil.isNotEmpty(acceptAllFilters)){
			this.acceptAllFilters.addAll(acceptAllFilters);
		}
	}
	public List<ObjectFilter<T>> getAcceptOneFilters() {
		return acceptOneFilters;
	}
	public void setAcceptOneFilters(List<ObjectFilter<T>> acceptOneFilters) {
		this.acceptOneFilters.clear();
		if(CollectionUtil.isNotEmpty(acceptOneFilters)){
			this.acceptOneFilters.addAll(acceptOneFilters);
		}
	}
	public List<ObjectFilter<T>> getAcceptNotFilters() {
		return acceptNotFilters;
	}
	public void setAcceptNotFilters(List<ObjectFilter<T>> acceptNotFilters) {
		this.acceptNotFilters.clear();
		if(CollectionUtil.isNotEmpty(acceptNotFilters)){
			this.acceptNotFilters.addAll(acceptNotFilters);
		}
	}
}
