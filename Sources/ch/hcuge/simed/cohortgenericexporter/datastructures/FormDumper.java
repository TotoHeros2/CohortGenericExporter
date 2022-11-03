package ch.hcuge.simed.cohortgenericexporter.datastructures;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol._EOCheapCopyMutableArray;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

//import ch.hcuge.simed.cohort.sscs.eo.Inclusion;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortField;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortForm;
import ch.hcuge.simed.cohortgenericexporter.utilities.ArrayOperations;
import ch.hcuge.simed.cohortgenericexporter.utilities.ExporterConstante;
import ch.hcuge.simed.foundation.extendableenum.ExtendableEnum;

public class FormDumper <A extends EOGenericRecord>{
	
	private CohortForm _form;
	private NSMutableDictionary<A, NSMutableDictionary<String, NSMutableArray<String>>> _dumpedValues;
	private Object _tempObject;
	private Integer _tempCount = 0;
	private CohortField _tempField;
	
	public FormDumper(CohortForm form){
		this._form = form;
		this._dumpedValues = new NSMutableDictionary<A ,NSMutableDictionary<String, NSMutableArray<String>>>();
	}
	
	public void dump(A eoObject) {
		_dumpedValues.put(eoObject, new NSMutableDictionary<String, NSMutableArray<String>>());
		for(CohortField aField : _form.fields()) {
			_dumpedValues.get(eoObject).put(aField.name(), new NSMutableArray<String>());
			String[] pathTable = aField.name().split("\\.");
			recursivedumper(((EOGenericRecord) eoObject), pathTable, aField, eoObject);
		}
	}
	
	//Fonction qui remonte le keypath donné comme nom de champ dans le Json du form et se relance recursivement jusqu'à atteindre la fin de chaîne et stocker les données dans l'objet
	private void recursivedumper(EOGenericRecord object, String[] pathTable, CohortField field, A originobject) {
		if(pathTable.length > 1) {
			String[] newpathTable = ArrayOperations.elementRemoval(pathTable, 0);
			Object nextObjectinstance = object.valueForKey(pathTable[0]);
			if (nextObjectinstance instanceof EOGenericRecord) {
				EOGenericRecord castNextobjectInstance= (EOGenericRecord) nextObjectinstance;
				recursivedumper(castNextobjectInstance, newpathTable, field, originobject);
			} else if(nextObjectinstance instanceof _EOCheapCopyMutableArray) {
				 _EOCheapCopyMutableArray objectTable = (_EOCheapCopyMutableArray) nextObjectinstance;
				for(int i = 0; i < objectTable.size(); i++) {
					EOGenericRecord newObject = (EOGenericRecord) objectTable.get(i);
					recursivedumper(newObject, newpathTable, field, originobject);
				}
			}
		} else if (pathTable.length == 1) {
			if(pathTable[0].equals(ExporterConstante.EMPTY_STRING))
				finalDump(originobject, field, ExporterConstante.SPACE_DOT_SPACE);
			else
				finalDump(originobject, field,object.valueForKey(pathTable[0]));			
		} 
	}
	
	private void finalDump(A eoObject, CohortField field, Object value) {
		if(value instanceof ExtendableEnum) {
			ExtendableEnum castValue = (ExtendableEnum) value;
			_dumpedValues.get(eoObject).get(field.name()).add(castValue.getCode());
		}
		else {
			String toDump;
			if(value == null)
				toDump = ExporterConstante.EMPTY_STRING;
			else 
				toDump = value.toString();
			_dumpedValues.get(eoObject).get(field.name()).add(toDump);
		}
		instanceCountIncrement(field, eoObject);
	}
	
	public NSDictionary<A, NSDictionary<String, NSArray<String>>> returnDumpedForms(){
		NSMutableDictionary<A, NSDictionary<String, NSArray<String>>> tempDictionary = new NSMutableDictionary<>();
		NSArray<A> objectKeys = _dumpedValues.allKeys();
		for(A objectKey : objectKeys) {
			NSArray<String> fieldKeys = _dumpedValues.get(objectKey).allKeys();
			NSMutableDictionary<String, NSArray<String>> tempSubDictionary = new NSMutableDictionary<>();
			for(String fieldKey : fieldKeys) {
				tempSubDictionary.put(fieldKey, _dumpedValues.get(objectKey).get(fieldKey).immutableClone());
			}
			tempDictionary.put(objectKey, tempSubDictionary.immutableClone());
		}
		return tempDictionary.immutableClone();
	}
		
	private void instanceCountIncrement(CohortField field, Object object) {
		if(object == _tempObject && field == _tempField) {
			_tempCount += 1;
			Integer count = field.instanceCount() == null ? 1 : field.instanceCount();	
			if(_tempCount > count) {
				field.setInstanceCount(_tempCount);
			}
		} else {
			_tempField = field;
			_tempObject = object;
			_tempCount = 1;
			if(field.instanceCount() == null)
				field.setInstanceCount(1);
		}
	}
}

